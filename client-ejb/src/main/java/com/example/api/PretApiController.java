package com.example.api;

import java.time.LocalDate;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

@Path("/pret")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PretApiController {

        private static final String BASE_URL = "http://localhost:5000/api";


        // @GET
        // @Path("/payementPret")
        // public Response payementPret(
        //                 @QueryParam("idComptePret") int idComptePret,
        //                 @QueryParam("idCompteCourant") int idCompteCourant) {
        //         try {
        //                 HttpResponse<String> response = Unirest.get(BASE_URL + "/Amortissement/rembourssement")
        //                                 .queryString("idComptePret", idComptePret)
        //                                 .queryString("idCompteCourant", idCompteCourant)
        //                                 .asString();

        //                 return Response.status(response.getStatus())
        //                                 .entity(response.getBody())
        //                                 .build();

        //         } catch (Exception e) {
        //                 return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        //                                 .entity("Erreur GET API C# : " + e.getMessage())
        //                                 .build();
        //         }
        // }

        @GET
        @Path("/payementPret")
        public Response payementPret(
                        @QueryParam("idComptePret") int idComptePret) {
                try {
                        HttpResponse<String> response = Unirest.get(BASE_URL + "/Amortissement/rembourssement")
                                        .queryString("idComptePret", idComptePret)
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

        @GET
        @Path("/byIdCompteAndAmort")
        public Response getPretByClient(
                        @QueryParam("idCompte") int idCompte,
                        @QueryParam("idAmortissement") int idAmortissement

        ) {
                try {
                        HttpResponse<String> response = Unirest.get(BASE_URL + "/TransactionPret/byIdCompteAndAmort")
                                        .queryString("idCompte", idCompte)
                                        .queryString("idAmortissement", idAmortissement)
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

        // rembourssement du pret par mois
        // url reel
        // http://localhost:5000/api/Amortissement/update?idAmortissement=1
        @PUT
        @Path("/remboursser")
        public Response remboursser(
                        String amortissement) {
                try {
                        HttpResponse<String> response = Unirest.put(BASE_URL + "/Amortissement/remboursser")
                                        .header("Content-Type", "application/json")
                                        .body(amortissement)
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

        // fetcher le tableau d'amortissement du compte d'un client
        // url réel
        // http://localhost:5000/api/Amortissement/byCompte?idComptePret=1
        @GET
        @Path("/byClientSolde")
        public Response getPretByClient(
                        @QueryParam("idClient") int idClient, @QueryParam("date") String date) {
                try {
                        HttpResponse<String> response = Unirest.get(BASE_URL + "/ComptePret/byClientSolde")
                                        .queryString("idClient", idClient)
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

        // fetcher le tableau d'amortissement du compte d'un client
        // url réel
        // http://localhost:5000/api/Amortissement/byCompte?idComptePret=1
        @GET
        @Path("/byCompte")
        public Response getSolde(
                        @QueryParam("idComptePret") int idComptePret) {
                try {
                        HttpResponse<String> response = Unirest.get(BASE_URL + "/Amortissement/byCompte")
                                        .queryString("idCompte", idComptePret)
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

        // getSolde du pret par une date et par le compte du client
        // l'url reel depuis l'autre sa
        // http://localhost:5000/api/Amortissement/solde?idComptePret=1
        @GET
        @Path("/getSolde")
        public Response getSolde(
                        @QueryParam("idComptePret") int idComptePret,
                        @QueryParam("date") String date) {
                try {
                        if (date == null)
                                date = LocalDate.now().toString();
                        HttpResponse<String> response = Unirest.get(BASE_URL + "/Amortissement/solde")
                                        .queryString("idComptePret", idComptePret)
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

        // creation d'un compte de pret, intialisations du tableau d'amortissement
        // url d'origin
        // http://localhost:5000/api/ComptePret/askPret?idCompteCourant=1
        @POST
        @Path("/askPret")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response createTransactionEpargne(String json) {
                try {
                        // Affiche le JSON reçu
                        System.out.println(json);
                        System.out.flush();
                        // Envoie directement au serveur C#
                        HttpResponse<String> response = Unirest.post(BASE_URL + "/ComptePret/askPret")
                                        .header("Content-Type", "application/json")
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

        // @POST
        // @Path("/askPret")
        // @Consumes(MediaType.APPLICATION_JSON)
        // @Produces(MediaType.APPLICATION_JSON)
        // public Response createTransactionEpargne(String json,
        // @QueryParam("idCompteCourant") int idCompteCourant) {
        // try {
        // // Affiche le JSON reçu
        // System.out.println(json);
        // System.out.flush();
        // // Envoie directement au serveur C#
        // HttpResponse<String> response = Unirest.post(BASE_URL +
        // "/ComptePret/askPret")
        // .header("Content-Type", "application/json")
        // .queryString("idCompteCourant", idCompteCourant)
        // .body(json)
        // .asString();

        // return Response.status(response.getStatus())
        // .entity(response.getBody())
        // .build();

        // } catch (Exception e) {
        // return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        // .entity("Erreur POST API C# : " + e.getMessage())
        // .build();
        // }
        // }
}
