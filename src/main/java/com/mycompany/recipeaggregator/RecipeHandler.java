package com.mycompany.recipeaggregator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private RecipeDAO dao = new RecipeDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Verifica se a requisição é um GET
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Recipe> recipes = dao.list(); // Busca todas as receitas no banco de dados
            String jsonResponse = objectMapper.writeValueAsString(recipes); // Converte a lista para JSON

            // Define cabeçalhos HTTP
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length); // Envia status 200

            // Envia a resposta JSON
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } 
        // Verifica se a requisição é um POST
        else if ("POST".equals(exchange.getRequestMethod())) {
            InputStream requestBody = exchange.getRequestBody();
            
            // Lê o corpo da requisição e converte para String
            String json = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            // Converte JSON para um objeto Recipe
            Recipe recipe = objectMapper.readValue(json, Recipe.class);
            
            // Insere a nova receita no banco de dados
            dao.insert(recipe);

            exchange.sendResponseHeaders(201, -1); // Retorna status 201
        } 
        // Verifica se a requisição é um PUT
        else if ("PUT".equals(exchange.getRequestMethod())) {
            // Extrai o ID da receita a partir da URL
            int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
            InputStream requestBody = exchange.getRequestBody();
            
            // Lê o corpo da requisição e converte para String
            String json = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            
            // Converte JSON para um objeto Recipe e define o ID
            Recipe recipe = objectMapper.readValue(json, Recipe.class);
            recipe.setId(id);
            
            // Atualiza a receita no banco de dados
            dao.update(recipe);
            
            exchange.sendResponseHeaders(204, -1); // Retorna status 204
        } 
        // Verifica se a requisição é um DELETE
        else if ("DELETE".equals(exchange.getRequestMethod())) {
            // Extrai o ID da receita a partir da URL
            int id = Integer.parseInt(exchange.getRequestURI().getPath().split("/")[2]);
            
            // Remove a receita do banco de dados
            dao.delete(id);
            
            exchange.sendResponseHeaders(204, -1); // Retorna status 204
        }
    }
}
