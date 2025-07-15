package com.mycompany.recipeaggregator;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class RecipeServlet extends HttpServlet {

    RecipeDAO dao = new RecipeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        List<Recipe> recipes = dao.list();
        String json = mapper.writeValueAsString(recipes);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = mapper.readValue(request.getReader(), Recipe.class);

        dao.insert(recipe);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write("{\"message\": \"Receita criada com sucesso\"}");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = mapper.readValue(request.getReader(), Recipe.class);

        dao.update(recipe);

        response.getWriter().write("{\"message\": \"Receita atualizada com sucesso\"}");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            dao.delete(id);

            response.getWriter().write("{\"message\": \"Receita deletada com sucesso\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"ID nã\"}");

        }
    }

}
