package com.mycompany.recipeaggregator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.recipeaggregator.config.DatabaseConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RecipeServlet extends HttpServlet {

    RecipeDAO dao = new RecipeDAO(DatabaseConfig.PROD_DB_URL);
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Recipe> recipes = dao.list();
            response.setContentType("application/json");
            response.getWriter().write(mapper.writeValueAsString(recipes));
        } catch (SQLException e) {
            sendError(response, 500, "Erro ao listar receitas", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Recipe recipe = mapper.readValue(request.getReader(), Recipe.class);
            dao.insert(recipe);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"message\": \"Receita criada com sucesso\"}");
        } catch (SQLException e) {
            sendError(response, 500, "Erro ao inserir receita", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Recipe recipe = mapper.readValue(request.getReader(), Recipe.class);
            dao.update(recipe);
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
