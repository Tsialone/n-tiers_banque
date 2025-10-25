package com.example.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    private static final String ORIGIN = "http://localhost:5173";

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        // Remove any existing CORS headers first to prevent duplicates
        response.getHeaders().remove("Access-Control-Allow-Origin");
        response.getHeaders().remove("Access-Control-Allow-Credentials");
        response.getHeaders().remove("Access-Control-Allow-Headers");
        response.getHeaders().remove("Access-Control-Allow-Methods");
        
        // Now set them once
        response.getHeaders().putSingle("Access-Control-Allow-Origin", ORIGIN);
        response.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        // Handle preflight requests
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(200);
        }
    }
}