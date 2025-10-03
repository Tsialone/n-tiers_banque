package com.example.api;

import java.util.Map;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.JsonNode;

@Path("/epargne")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EpargneApiController {

    private static final String BASE_URL = "http://172.17.0.1:6000/api";
    // trouver les comptes d'un client
    // GET solde
    @GET
    @Path("/byClient")
    public Response getEpargnesByClient(
            @QueryParam("idClient") int idClient) {
        try {
            HttpResponse<String> response = Unirest.get(BASE_URL + "/CompteEpargne/byClient")
                    .queryString("idClient", idClient)
                    .asString();

            return Response.status(response.getStatus())
                    .entity(response.getBody())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur GET API C# : " + e.getMessage())
                    .build();
        }
    }

    // creation d'un transaction epargne
    @POST
    @Path("/transaction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTransactionEpargne(String json,
            @QueryParam("idCompteCourant") int idCompteCourant) {
        try {
            // Affiche le JSON reçu
            System.out.println(json);
            System.out.flush();
            // Envoie directement au serveur C#
            HttpResponse<String> response = Unirest.post(BASE_URL + "/TransactionEpargne")
                    .header("Content-Type", "application/json")
                    .queryString("idCompteCourant", idCompteCourant)
                    .body(json) 
                    .asString();

            return Response.status(response.getStatus())
                    .entity(response.getBody())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur POST API C# : " + e.getMessage())
                    .build();
        }
    }
    // creation d'un compte
    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createEpargne(String json,
            @QueryParam("idCompteCourant") int idCompteCourant) {
        try {
            // Affiche le JSON reçu
            System.out.println(json);
            System.out.flush();
            // Envoie directement au serveur C#
            HttpResponse<String> response = Unirest.post(BASE_URL + "/CompteEpargne")
                    .header("Content-Type", "application/json")
                    .queryString("idCompteCourant", idCompteCourant)
                    .body(json) 
                    .asString();

            return Response.status(response.getStatus())
                    .entity(response.getBody())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur POST API C# : " + e.getMessage())
                    .build();
        }
    }
    // GET solde
    @GET
    @Path("/getSolde")
    public Response getSolde(
            @QueryParam("idClient") int idClient,
            @QueryParam("idCompteEpargne") int idCompteEpargne,
            @QueryParam("date") String date) {
        try {
            HttpResponse<String> response = Unirest.get(BASE_URL + "/TransactionEpargne/solde")
                    .queryString("idClient", idClient)
                    .queryString("idCompteEpargne", idCompteEpargne)
                    .queryString("date", date)
                    .asString();

            return Response.status(response.getStatus())
                    .entity(response.getBody())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur GET API C# : " + e.getMessage())
                    .build();
        }
    }

}
