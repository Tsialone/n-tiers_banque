package com.example.controllers;

import java.time.LocalDate;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import com.example.dto.CompteCourantDto;
import com.example.mappers.CompteCourantMapper;
import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.views.CompteCourantView;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.CompteCourantServiceRemote;


@Path("/comptes-courants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompteCourantController {

    @EJB(lookup = "java:global/server-ejb/CompteCourantService!com.example.remotes.CompteCourantServiceRemote")
    private CompteCourantServiceRemote service;

    @EJB(lookup = "java:global/server-ejb/ClientCourantService!com.example.remotes.ClientCourantServiceRemote")
    private ClientCourantServiceRemote clientCourantService;

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
    @Path("/client")
    public Response getComptesByClient(@QueryParam("idClient") Integer idClient, @QueryParam("date") String date)
            throws Exception {

        List<CompteCourant> comptes = service.getComptesByClient(idClient);

        LocalDate theDate = null;
        if (date != null && !date.isEmpty()) {
            theDate = LocalDate.parse(date);
        }
        List<CompteCourantView> comptes_views = new ArrayList<>();
        for (CompteCourant compte_entity : comptes) {
            CompteCourantView temp_view = new CompteCourantView();
            double solde = clientCourantService.getSoldeByIdClientAndIdCompte(compte_entity.getIdCompte(), idClient,
                    theDate);
            temp_view.setNom(compte_entity.getNom());
            temp_view.setIdCompte(compte_entity.getIdCompte());
            temp_view.setCapital(compte_entity.getCapital());
            temp_view.setDateOuverture(compte_entity.getDateOuverture());
            temp_view.setDecouvertAutorise(compte_entity.getDecouvertAutorise());
            temp_view.setSolde(solde);
            comptes_views.add(temp_view);
        }
        return Response.ok(comptes_views).build();
    }

    // Ajouter ou mettre à jour un compte
    @POST
    public Response saveCompte(CompteCourantDto dto) {
        try {
            ClientCourant client = clientCourantService.getClientById(dto.getIdClient());
            CompteCourant entity = CompteCourantMapper.toEntity(dto, client);
            CompteCourant saved = service.saveCompte(entity);

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
        return Response.ok(dto).build();
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
