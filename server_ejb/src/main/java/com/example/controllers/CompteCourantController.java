package com.example.rest;

import com.example.models.CompteCourant;
import com.example.service.CompteCourantService;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/compte-courant")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompteCourantController {

    @EJB
    private CompteCourantService service;

    @GET
    public Response getAllComptes() {
        List<CompteCourant> comptes = service.getAllComptes();
        return Response.ok(comptes).build();
    }

    // Récupérer un compte par ID
    @GET
    @Path("/{idCompte}")
    public Response getCompteById(@PathParam("idCompte") Integer idCompte) {
        try {
            CompteCourant compte = service.getCompteById(idCompte);
            return Response.ok(compte).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    // Récupérer tous les comptes d'un client
    @GET
    @Path("/client/{idClient}")
    public Response getComptesByClient(@PathParam("idClient") Integer idClient) {
        List<CompteCourant> comptes = service.getComptesByClient(idClient);
        return Response.ok(comptes).build();
    }

    // Ajouter ou mettre à jour un compte
    @POST
    public Response saveCompte(CompteCourant compte) {
        CompteCourant saved = service.saveCompte(compte);
        return Response.ok(saved).build();
    }

    // Supprimer un compte
    @DELETE
    @Path("/{idCompte}")
    public Response deleteCompte(@PathParam("idCompte") Integer idCompte) {
        try {
            CompteCourant compte = service.getCompteById(idCompte);
            service.deleteCompte(compte);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
