package com.mycompany.recipeaggregator.e2e;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeE2ETest extends BaseE2ETest {

    @Test
    void shouldCreateAndFindRecipe() throws Exception {

        String ingredientJson = """
                        {
                          "name": "Farinha"
                        }
                """;

        String ingredientResponse = sendPost("/ingredients", ingredientJson, 201);
        int ingredientId = extractId(ingredientResponse);

        String json = """
                        {
                          "name": "Exemplo",
                          "description": "Receita rápida",
                          "preparationTime": 10,
                          "portions": 1,
                          "ingredients": [
                            {
                              "ingredientId": %d,
                              "quantity": 2,
                              "unit": "unidade"
                            }
                          ]
                        }
                """.formatted(ingredientId);

        String postResponse = sendPost("/recipes", json);

        assertNotNull(postResponse);
        assertTrue(postResponse.contains("id"));

        int id = extractId(postResponse);

        String getResponse = sendGet("/recipes/" + id);

        assertNotNull(getResponse);
        assertTrue(getResponse.contains("Exemplo"));
        assertTrue(getResponse.contains("Receita rápida"));
        assertTrue(getResponse.contains("10"));
        assertTrue(getResponse.contains("1"));

        assertTrue(getResponse.contains("ingredientId"));
        assertTrue(getResponse.contains("quantity"));
        assertTrue(getResponse.contains("unit"));
    }

    private String sendPost(String path, String json) throws Exception {
        return sendPost(path, json, 201);
    }

    private String sendPost(String path, String json, int expectedStatus) throws Exception {
        URL url = new URL(baseUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        int status = conn.getResponseCode();
        assertEquals(expectedStatus, status);

        return readResponse(conn);
    }

    private String sendGet(String path) throws Exception {
        URL url = new URL(baseUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        assertEquals(200, status);

        return readResponse(conn);
    }

    private String readResponse(HttpURLConnection conn) throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        return response.toString();
    }

    private int extractId(String json) {
        String idStr = json.replaceAll(".*\"id\":(\\d+).*", "$1");
        return Integer.parseInt(idStr);
    }
}
