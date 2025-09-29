package com.example.service;

import com.example.models.TransactionCourant;
import com.example.repositories.TransactionCourantRepository;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class TransactionCourantService {

    @EJB
    private TransactionCourantRepository repository;

    public List<TransactionCourant> getAllTransactions() {
        return repository.getAll();
    }

    // Récupérer une transaction par ID
    public TransactionCourant getTransactionById(Integer id) {
        TransactionCourant t = repository.findById(id);
        if (t == null) {
            throw new IllegalArgumentException("Transaction non trouvée : " + id);
        }
        return t;
    }

    // Récupérer toutes les transactions d'un compte
    public List<TransactionCourant> getTransactionsByCompte(Integer idCompte) {
        return repository.findByCompteId(idCompte);
    }

    // Ajouter ou mettre à jour une transaction
    public TransactionCourant saveTransaction(TransactionCourant transaction) {
        repository.save(transaction);
        return transaction;
    }

    // Supprimer une transaction
    public void deleteTransaction(TransactionCourant transaction) {
        repository.delete(transaction);
    }
}
