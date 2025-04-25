package com.mycompany.recipeaggregator;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(7000), 0);
        
        server.createContext("/recipes", new RecipeHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("Rodando na porta 7000");
    }
}
