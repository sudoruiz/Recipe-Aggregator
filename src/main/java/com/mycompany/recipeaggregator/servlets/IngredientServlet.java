package com.mycompany.recipeaggregator.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.recipeaggregator.config.DatabaseConfig;
import com.mycompany.recipeaggregator.dao.IngredientDAO;
import com.mycompany.recipeaggregator.dto.IngredientCreateDTO;
import com.mycompany.recipeaggregator.dto.IngredientResponseDTO;
import com.mycompany.recipeaggregator.repository.IngredientRepository;
import com.mycompany.recipeaggregator.service.IngredientService;
import jakarta.servlet.http.*;

import java.io.IOException;

public class IngredientServlet extends HttpServlet {

    private IngredientService service;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() {
        IngredientRepository repository =
                new IngredientDAO(DatabaseConfig.PROD_DB_URL);

        this.service = new IngredientService(repository);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        String mostUsedParam = request.getParameter("mostUsed");

        try {

            Object result;

            if ("true".equalsIgnoreCase(mostUsedParam)) {
                result = service.listMostUsed();
            } else {
                result = service.listAll();
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter()
                    .write(mapper.writeValueAsString(result));

        } catch (Exception e) {
            sendError(response, 500, "Error listing ingredients", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        try {

            IngredientCreateDTO dto =
                    mapper.readValue(request.getReader(),
                            IngredientCreateDTO.class);

            IngredientResponseDTO created =
                    service.create(dto);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.getWriter()
                    .write(mapper.writeValueAsString(created));

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage(), e);
        } catch (Exception e) {
            sendError(response, 500, "Error creating ingredient", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {

        try {

            String idParam = request.getParameter("id");

            if (idParam == null) {
                throw new IllegalArgumentException("ID not provided");
            }

            int id = Integer.parseInt(idParam);

            service.delete(id);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter()
                    .write("{\"message\":\"Ingrediente removido com sucesso\"}");

        } catch (NumberFormatException e) {
            sendError(response, 400, "Invalid ID", e);
        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage(), e);
        } catch (Exception e) {
            sendError(response, 500, "Error deleting ingredient", e);
        }
    }

    private void sendError(HttpServletResponse response,
                           int status,
                           String message,
                           Exception e)
            throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter()
                .write("{\"error\": \"" + message + "\"}");

        if (e != null) {
            e.printStackTrace();
        }
    }
}

