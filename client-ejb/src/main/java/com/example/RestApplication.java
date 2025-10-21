package com.example;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api") // racine de l'API REST
public class RestApplication extends Application {
    // Pas besoin de méthodes ici, juste l'annotation
}
