package com.andreadisimone;

import com.andreadisimone.util.DatabaseConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class App 
{
    public static void main(String[] args) {
        DatabaseConnection.init("config.properties");
        System.out.println("Properties inizializzate");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Per gestire conversioni json data/timestamp 

        // javalin starta http server
        // da routes
        // da context
        // jackson converte oggetti java in json e json in oggetti java
        // object mapper decide come questi vengono convertiti
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper, true));
            config.http.defaultContentType = "application/json";
        });

        UserDemoJDBCController userDemoJDBCController = new UserDemoJDBCController();
        userDemoJDBCController.registerRoutes(app);

        app.start(8080); //app è metodo di Javalin

    }
    
}
