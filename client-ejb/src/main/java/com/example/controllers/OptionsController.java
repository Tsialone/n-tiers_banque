package com.example.controllers;

import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")  // racine de l'API
public class OptionsController {

    @OPTIONS
    @Path("{any: .*}")  // match tous les chemins
    public Response handleOptions() {
        return Response.ok().build();
    }
}
