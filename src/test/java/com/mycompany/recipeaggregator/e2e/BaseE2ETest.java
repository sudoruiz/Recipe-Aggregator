package com.mycompany.recipeaggregator.e2e;

import com.mycompany.recipeaggregator.config.DatabaseConfigTest;
import com.mycompany.recipeaggregator.servlets.IngredientServlet;
import com.mycompany.recipeaggregator.servlets.RecipeServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;

public abstract class BaseE2ETest {

    private static Tomcat tomcat;
    protected static String baseUrl;

    @BeforeAll
    static void startServer() throws Exception {
        DatabaseConfigTest.configure();

        tomcat = new Tomcat();
        tomcat.setPort(0);

        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());

        Tomcat.addServlet(ctx, "recipeServlet", new RecipeServlet());
        ctx.addServletMappingDecoded("/recipes/*", "recipeServlet");

        Tomcat.addServlet(ctx, "ingredientServlet", new IngredientServlet());
        ctx.addServletMappingDecoded("/ingredients", "ingredientServlet");

        tomcat.start();

        int port = tomcat.getConnector().getLocalPort();
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    static void stopServer() throws Exception {
        if (tomcat != null) {
            tomcat.stop();
            tomcat.destroy();
        }
    }
}
