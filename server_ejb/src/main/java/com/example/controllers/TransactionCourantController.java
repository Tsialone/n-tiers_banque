package com.example.rest;

import com.example.dto.TransactionCourantDto;
import com.example.models.CompteCourant;
import com.example.models.TransactionCourant;
import com.example.service.TransactionCourantService;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

import com.example.service.CompteCourantService;

@Path("/transactions-courant")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionCourantController {

    @EJB
    private TransactionCourantService service;
    @EJB
    private CompteCourantService compteCourantService;

    // DTO → Entité
    public TransactionCourant toEntity(TransactionCourantDto dto) {
        TransactionCourant transaction = new TransactionCourant();

        if (dto.getIdTransaction() != null) {
            transaction.setIdTransaction(dto.getIdTransaction());
        }
        // System.out.println(dto);
        // Récupérer le compte depuis l'ID
        CompteCourant compte = compteCourantService.getCompteById(dto.getIdCompte());
        transaction.setCompte(compte);

        transaction.setLibelle(dto.getLibelle());
        transaction.setMontant(dto.getMontant());
        transaction.setSens(dto.getSens());
        transaction.setDateTransaction(dto.getDateTransaction() != null
                ? dto.getDateTransaction()
                : java.time.LocalDate.now());

        return transaction;
    }

    // Entité → DTO
    public TransactionCourantDto toDto(TransactionCourant transaction) {
        TransactionCourantDto dto = new TransactionCourantDto();
        dto.setIdTransaction(transaction.getIdTransaction());
        dto.setIdCompte(transaction.getCompte().getIdCompte());
        dto.setLibelle(transaction.getLibelle());
        dto.setMontant(transaction.getMontant());
        dto.setSens(transaction.getSens());
        dto.setDateTransaction(transaction.getDateTransaction());
        return dto;
    }

    @GET
    public Response getAllTransactions() {
        List<TransactionCourant> transactions = service.getAllTransactions();
        return Response.ok(transactions).build();
    }

    // Récupérer une transaction par ID
    @GET
    @Path("/{id}")
    public Response getTransaction(@PathParam("id") Integer id) {
        try {
            TransactionCourant t = service.getTransactionById(id);
            return Response.ok(t).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    // Récupérer toutes les transactions d'un compte
    @GET
    @Path("/compte")
    public Response getTransactionsByCompte(@QueryParam("idCompte") Integer idCompte) {
        List<TransactionCourant> transactions = service.getTransactionsByCompte(idCompte);
        return Response.ok(transactions).build();
    }

    // Ajouter ou mettre à jour une transaction
    @POST
    public Response saveTransaction(TransactionCourantDto transactionDto) {
        try {
            TransactionCourant saved = service.saveTransaction(toEntity(transactionDto));
            return Response.ok(saved).build();
        } catch (Exception e) {
            // Retourne 500 avec un message JSON
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    // Supprimer une transaction
    @DELETE
    @Path("/{id}")
    public Response deleteTransaction(@PathParam("id") Integer id) {
        try {
            TransactionCourant t = service.getTransactionById(id);
            service.deleteTransaction(t);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
