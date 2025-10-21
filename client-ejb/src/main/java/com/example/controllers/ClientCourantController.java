package com.example.controllers;

import com.example.models.ClientCourant;
import com.example.remotes.ClientCourantServiceRemote;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clients-courants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientCourantController {

    @EJB(lookup = "java:global/server-ejb/ClientCourantService!com.example.remotes.ClientCourantServiceRemote")
    private ClientCourantServiceRemote service;

    @GET
    @Path("/session_auth")
    public Response getUtilisateur() {
        try {
            Object utilisateur = service.getUtilisateur();

            if (utilisateur == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Aucun utilisateur trouvé pour cette session")
                        .build();
            }

            return Response.ok(utilisateur).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/login")
    public Response loginByIdClient(@QueryParam("idClient") int idClient) {

        try {
            Object utilisateur = service.login(idClient);
            if (utilisateur == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Aucun utilisateur trouvé")
                        .build();
            }
            return Response.ok(service.login(idClient)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    public Response getAllClients() {

        try {
            return Response.ok(service.getAllClients()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            ClientCourant client = service.getClientById(id);
            return Response.ok(client).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }

    }

}
