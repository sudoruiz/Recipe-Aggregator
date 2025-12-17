package com.mycompany.recipeaggregator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.recipeaggregator.config.DatabaseConfig;
import com.mycompany.recipeaggregator.dao.IngredientDAO;
import com.mycompany.recipeaggregator.dto.IngredientDTO;
import com.mycompany.recipeaggregator.dto.Mapper;
import com.mycompany.recipeaggregator.models.Ingredient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class IngredientServlet extends HttpServlet {

    private final IngredientDAO dao = new IngredientDAO(DatabaseConfig.PROD_DB_URL);
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Ingredient> ingredients = dao.list();
            List<IngredientDTO> dtoList = ingredients.stream()
                    .map(Mapper::toDTO)
                    .toList();

            response.setContentType("application/json");
            response.getWriter().write(mapper.writeValueAsString(dtoList));

        } catch (SQLException e) {
            sendError(response, 500, "Erro ao listar ingredientes", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            IngredientDTO dto = mapper.readValue(request.getReader(), IngredientDTO.class);
            Ingredient ingredient = Mapper.toEntity(dto);

            dao.insert(ingredient);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Ingrediente criado com sucesso\"}");

        } catch (SQLException e) {
            sendError(response, 500, "Erro ao criar ingrediente", e);
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

            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Ingrediente removido com sucesso\"}");

        } catch (NumberFormatException e) {
            sendError(response, 400, "ID inválido", e);
        } catch (SQLException e) {
            sendError(response, 500, "Erro ao remover ingrediente", e);
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

