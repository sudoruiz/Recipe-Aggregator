package com.mycompany.recipeaggregator.servlets;

import com.mycompany.recipeaggregator.repository.RecipeIngredientRepository;
import com.mycompany.recipeaggregator.service.RecipeIngredientService;
import com.mycompany.recipeaggregator.repository.RecipeRepository;
import com.mycompany.recipeaggregator.service.RecipeService;
import com.mycompany.recipeaggregator.config.DatabaseConfig;
import com.mycompany.recipeaggregator.models.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.recipeaggregator.dao.*;
import com.mycompany.recipeaggregator.dto.*;
import jakarta.servlet.http.*;
import java.sql.SQLException;
import java.io.IOException;
import jakarta.servlet.*;
import java.util.List;

public class RecipeServlet extends HttpServlet {

    private RecipeIngredientService recipeIngredientService;
    private RecipeService recipeService;
    private ObjectMapper mapper;

    @Override
    public void init() {
        RecipeRepository repo =
                new RecipeDAO(DatabaseConfig.PROD_DB_URL);

        RecipeIngredientRepository ingredientRepo =
                new RecipeIngredientDAO(DatabaseConfig.PROD_DB_URL);

        this.recipeIngredientService = new RecipeIngredientService(ingredientRepo);
        this.recipeService = new RecipeService(repo);
        this.mapper = new ObjectMapper();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<RecipeResponseDTO> dtoList = recipeService.listRecipes();
            response.setContentType("application/json");
            response.getWriter().write(mapper.writeValueAsString(dtoList));

        } catch (SQLException e) {
            sendError(response, 500, "Erro ao listar receitas", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            RecipeCreateDTO dto = mapper.readValue(request.getReader(), RecipeCreateDTO.class);
            Recipe recipe = recipeService.createRecipe(dto);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String json = mapper.writeValueAsString(recipe);
            response.getWriter().write(json);
        } catch (SQLException e) {
            sendError(response, 500, "Error on insert recipe", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            sendError(response, 400, "Id not provided", null);
            return;
        }
        try {
            int id = Integer.parseInt(idParam);

            RecipeCreateDTO dto = mapper.readValue(request.getReader(), RecipeCreateDTO.class);

            recipeService.updateRecipe(id, dto);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Receita atualizada com sucesso\"}");
        } catch (SQLException e) {
            sendError(response, 500, "Erro ao atualizar receita", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            sendError(response, 400, "ID not provided", null);
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            recipeService.deleteRecipe(id);

            response.getWriter().write("{\"message\": \"Recipe deleted\"}");
        } catch (NumberFormatException e) {
            sendError(response, 400, "Invalid ID", e);
        } catch (SQLException e) {
            sendError(response, 500, "Error to delete recipe", e);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("PATCH".equalsIgnoreCase(request.getMethod())) {
            doPatchInternal(request, response);
            return;
        }
        super.service(request, response);
    }

    protected void doPatchInternal(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || !path.matches("/\\d+/ingredients")) {
            sendError(response, 404, "Invalid Route", null);
            return;
        }

        try {

            int recipeId = Integer.parseInt(path.split("/")[1]);

            RecipeIngredientPatchDTO dto =
                    mapper.readValue(request.getReader(), RecipeIngredientPatchDTO.class);

            recipeIngredientService.patchIngredients(recipeId, dto);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"message\":\"Updated Ingredients\"}"
            );

        } catch (NumberFormatException e) {

            sendError(response, 400, "Invalid recipe id", e);

        } catch (IllegalArgumentException e) {

            sendError(response, 400, e.getMessage(), e);

        } catch (SQLException e) {

            sendError(response, 500, "Error when updating ingredients", e);

        }
    }

    private void sendError(HttpServletResponse response, int status, String message, Exception e)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        if (e != null) {
            e.printStackTrace();
        }
    }
}
