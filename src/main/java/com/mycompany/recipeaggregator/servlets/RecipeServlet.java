package com.mycompany.recipeaggregator.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.recipeaggregator.config.DatabaseConfig;
import com.mycompany.recipeaggregator.dao.RecipeDAO;
import com.mycompany.recipeaggregator.dao.RecipeIngredientDAO;
import com.mycompany.recipeaggregator.dto.*;
import com.mycompany.recipeaggregator.models.Recipe;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeServlet extends HttpServlet {

    RecipeDAO dao = new RecipeDAO(DatabaseConfig.PROD_DB_URL);
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Recipe> recipes = dao.list();
            List<RecipeResponseDTO> dtoList = recipes.stream()
                    .map(Mapper::toDTO)
                    .collect(Collectors.toList());
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
            Recipe recipe = Mapper.toEntity(dto);
            dao.insert(recipe);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String json = mapper.writeValueAsString(recipe);
            response.getWriter().write(json);
        } catch (SQLException e) {
            sendError(response, 500, "Erro ao inserir receita", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            sendError(response, 400, "Id não fornecido", null);
            return;
        }
        try {
            int id = Integer.parseInt(idParam);

            RecipeCreateDTO dto = mapper.readValue(request.getReader(), RecipeCreateDTO.class);

            Recipe recipe = Mapper.toEntity(dto);
            recipe.setId(id);

            dao.update(recipe);
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
            sendError(response, 400, "ID não fornecido", null);
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            dao.delete(id);
            response.getWriter().write("{\"message\": \"Receita deletada com sucesso\"}");
        } catch (NumberFormatException e) {
            sendError(response, 400, "ID inválido", e);
        } catch (SQLException e) {
            sendError(response, 500, "Erro ao deletar receita", e);
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
            sendError(response, 404, "Invalide Route", null);
            return;
        }

        int recipeId = Integer.parseInt(path.split("/")[1]);

        try {
            RecipeIngredientPatchDTO dto =
                    mapper.readValue(request.getReader(), RecipeIngredientPatchDTO.class);

            RecipeIngredientDAO riDAO =
                    new RecipeIngredientDAO(DatabaseConfig.PROD_DB_URL);

            switch (dto.getAction()) {
                case "add" -> riDAO.addIngredient(
                        recipeId,
                        dto.getIngredientId(),
                        dto.getQuantity(),
                        dto.getUnit()
                );

                case "remove" -> riDAO.removeIngredient(
                        recipeId,
                        dto.getIngredientId()
                );

                case "update" -> riDAO.updateIngredient(
                        recipeId,
                        dto.getIngredientId(),
                        dto.getQuantity(),
                        dto.getUnit()
                );

                default -> {
                    sendError(response, 400, "Invalide Action", null);
                    return;
                }
            }

            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Updated Ingredients\"}");

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
