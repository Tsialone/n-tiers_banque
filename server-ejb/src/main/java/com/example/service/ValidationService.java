package com.example.service;

import com.example.remotes.ValidationServiceRemote;
import com.example.repositories.ClientCourantRepository;
import com.example.repositories.TransactionCourantRepository;
import com.example.repositories.ValidationRepository;
import com.example.mappers.ValidationMapper;
import com.example.models.TransactionCourant;
import com.example.server_dtos.ValidationDto;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class ValidationService implements ValidationServiceRemote {

    @EJB
    private TransactionCourantRepository repository;

    @EJB
    private ValidationRepository validationRepository;

    @EJB
    private ClientCourantRepository clientCourantRepository;

    private final Double maxVirementParJour = 100_000.0;

    @Override
    public ValidationDto saveValidation(Integer idTransactionCourant, String etat) throws Exception {
        TransactionCourant transaction = repository.findById(idTransactionCourant);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction non trouvée : " + idTransactionCourant);
        }
        if (!transaction.getValidations().isEmpty()) {
            throw new Exception("Vous avez deja : " + transaction.getLastValidation().getEtat());

        }

        if (etat.equals("valider")) {
            Double clientSolde = clientCourantRepository.findSoldeByIdClientAndIdCompte(
                    transaction.getCompte().getIdCompte(), transaction.getCompte().getClient().getIdClient(),
                    LocalDate.now());
            if (clientSolde < transaction.getMontant())
                throw new Exception("Solde insuffisant: " + clientSolde + " ar vs " + transaction.getMontant());
            List<TransactionCourant> allTransactionCourants = repository
                    .findVirementByClientIdAndDate(transaction.getCompte().getClient().getIdClient(), LocalDate.now());
            Double sum = transaction.getMontant();
            for (TransactionCourant transactionCourant : allTransactionCourants) {
                if (transaction.getLastValidation() != null
                        && transaction.getLastValidation().getEtat().equals("valider"))
                    sum += transactionCourant.getMontant();
            }
            System.out.println("voici la sum " + sum + " et maxParJour " + maxVirementParJour);
            if (sum > maxVirementParJour)
                throw new Exception(
                        "Vous avez atteint la limte de virement aujourdui " + sum + " vs " + maxVirementParJour);

        }

        Integer idVirement = transaction.getIdVirement();
        if (idVirement != null) {
            List<TransactionCourant> getByVirementId = repository.findByVirementId(idVirement);
            String test = "";
            for (TransactionCourant transactionCourant : getByVirementId) {
                ValidationDto dto = new ValidationDto();
                test += "\n " + transactionCourant.getIdTransaction();
                System.out.println("reto ilay trans eee " + transactionCourant.getIdTransaction());
                dto.setIdTransaction(transactionCourant.getIdTransaction());
                dto.setEtat(etat);
                dto.setDateValidation(LocalDate.now());
                validationRepository.save(ValidationMapper.toEntity(dto, transactionCourant));
            }
            // throw new Exception(test);
        } else {
            ValidationDto dto = new ValidationDto();
            dto.setIdTransaction(idTransactionCourant);
            dto.setEtat(etat);
            dto.setDateValidation(LocalDate.now());

            validationRepository.save(ValidationMapper.toEntity(dto, transaction));

            return dto;
        }

        return null;
    }
}
