package com.example.service;

import com.example.models.TransactionCourant;
import com.example.remotes.TransactionCourantServiceRemote;
import com.example.repositories.TransactionCourantRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;

import com.example.dto.TransactionCourantDto;
import com.example.mappers.TransactionCourantMapper;

@Stateless
public class TransactionCourantService  implements TransactionCourantServiceRemote {

    @EJB
    private TransactionCourantRepository repository;

    public List<TransactionCourantDto> getAllTransactionsByClient(int idClient) {
        List<TransactionCourantDto> transactionsCourantDto = new ArrayList<>();
        for (TransactionCourant transactionCourant : repository.getAll()) {
            if (transactionCourant.getCompte().getClient().getIdClient().equals(idClient)) {
                transactionsCourantDto.add(TransactionCourantMapper.toDto(transactionCourant));
            }
        }
        return transactionsCourantDto;
    }

    public List<TransactionCourantDto> getAllTransactions() {
        List<TransactionCourantDto> transactionsCourantDto = new ArrayList<>();
        for (TransactionCourant transactionCourant : repository.getAll()) {
            transactionsCourantDto.add(TransactionCourantMapper.toDto(transactionCourant));
        }
        return transactionsCourantDto;
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
