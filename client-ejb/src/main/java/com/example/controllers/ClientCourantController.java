package com.example.controllers;

import java.util.Map;

import com.example.annotations.TablePermission;
import com.example.models.ClientCourant;
import com.example.models.Direction;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.service.ClientCourantStatefulService;
import com.example.service.UserSession;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
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

    @Inject
    private UserSession userSession;

    @EJB(lookup = "java:global/server-ejb/ClientCourantService!com.example.remotes.ClientCourantServiceRemote")
    private ClientCourantServiceRemote service;

    @EJB(lookup = "java:global/server-ejb/ClientCourantStatefulService!com.example.remotes.ClientCourantStatefulServiceRemote")
    private ClientCourantStatefulServiceRemote session;

    @GET
    @TablePermission(table = "clients_courant")
    @Path("/check_instance")
    public Response checkInstance() {
        String id = session.getInstanceId();
        return Response.ok("Stateful EJB instance ID: " + id).build();
    }

    @GET
    // @TablePermission (table = "clients_courant")
    @Path("/checkDirection")
    public Response checkDirection(@QueryParam("page") String page) {
        // String id = session.getInstanceId();
        try {
            ClientCourant clientCourant = userSession.getClient();
            Direction direction = clientCourant.getDirection();
            if (!direction.getLibelle().equalsIgnoreCase(page) && !direction.getLibelle().equalsIgnoreCase("general")) {
                String message = "Vous avez pas la direction necaissaire pour visitez cette page: " + page
                        + ", votre direction actu: " + direction.getLibelle();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("error", message))
                        .build();
            }
            return Response.ok("Direction ok!").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }

    }

    // @GET
    // @Path("/session_auth")
    // public Response getUtilisateur() {
    // try {
    // Object utilisateur = session.getClient();

    // if (utilisateur == null) {
    // return Response.status(Response.Status.NOT_FOUND)
    // .entity("Aucun utilisateur trouvé pour cette session")
    // .build();
    // }

    // return Response.ok(utilisateur).build();

    // } catch (Exception e) {
    // return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    // .entity(e.getMessage())
    // .build();
    // }
    // }

    @GET
    @Path("/session_auth")
    public Response getUtilisateur() {
        ClientCourant client = userSession.getClient();
        if (client == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Aucun utilisateur trouvé pour cette session")
                    .build();
        }
        return Response.ok(client).build();
    }
    // @GET
    // @Path("/login")
    // public Response loginByIdClient(@QueryParam("idClient") int idClient) {

    // try {
    // ClientCourant clientCourant = service.getClientById(idClient);
    // session.setClient(clientCourant);
    // // if (utilisateur == null) {
    // // return Response.status(Response.Status.NOT_FOUND)
    // // .entity("Aucun utilisateur trouvé")
    // // .build();
    // // }
    // return Response.ok(session.getClient()).build();
    // } catch (Exception e) {
    // return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    // .entity(e.getMessage())
    // .build();
    // }
    // }
    @GET
    @Path("/login")
    public Response loginByIdClient(@QueryParam("idClient") int idClient) {
        try {
            ClientCourant client = service.getClientById(idClient);

            if (client == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Utilisateur non trouvé")
                        .build();
            }

            userSession.setClient(client); // Stocke dans le UserSession
            return Response.ok(userSession.getClient()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/logout")
    public Response logout() {
        try {
            userSession.clear();
            return Response.ok(Map.of("success", "deconnection reussie")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
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
