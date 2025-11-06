package com.example.service;

import com.example.models.Etat;
import com.example.models.TransactionCourant;
import com.example.models.ValidationTransaction;
import com.example.models.Virement;
import com.example.remotes.ValidationTransactionServiceRemote;
import com.example.remotes.ValidationVirementServiceRemote;
import com.example.repositories.EtatRepository;
import com.example.repositories.TransactionCourantRepository;
import com.example.repositories.ValidationTransactionRepository;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;
import java.time.LocalDate;

@Stateless
public class ValidationTransactionService implements ValidationTransactionServiceRemote {

    @EJB
    private ValidationTransactionRepository validationTransactionRepository;

    @EJB
    private TransactionCourantRepository transactionCourantRepository;

    @EJB
    private EtatRepository etatRepository;

    @EJB
    private ValidationVirementServiceRemote validationVirementServiceRemote;

    @Resource
    private EJBContext ejbContext;

    @Override
    public boolean saveValidation(Integer idTransaction, Integer idUtilisateur  , String action , boolean doubleChangeControl) throws Exception {
        try {

            TransactionCourant transaction = transactionCourantRepository.findById(idTransaction);
            if (transaction == null)
                throw new Exception("Transaction ne doit pas etre null");
            Etat etat = etatRepository.findByLibelle(action);

            if (etat == null)
                throw new Exception("État ne doit pas etre null");

            ValidationTransaction validation = transaction.changeEtat(etat, doubleChangeControl);
            validationTransactionRepository.save(validation);
            // if (action.equals("annuler")) {
            //     Virement virement =  transaction.getVirement();
            //     // throw new Exception("testttt " + virement.getIdVirement());
            //     if (virement != null) validationVirementServiceRemote.saveValidation(virement.getIdVirement() , null, idUtilisateur, "en_attente" , false);
                
            // }
            return true;

        } catch (Exception e) {
            ejbContext.setRollbackOnly();
            e.printStackTrace();
            throw e;
        }

    }

    // public ValidationTransaction updateValidation(ValidationTransaction
    // validation) throws Exception {
    // if (validation == null) throw new Exception("Validation introuvable");
    // return validationRepo.update(validation);
    // }

    // public ValidationTransaction getLastValidationByTransaction(Integer
    // idTransaction) {
    // return validationRepo.findLastValidation(idTransaction);
    // }

    // public List<ValidationTransaction> getAllValidations() {
    // return validationRepo.findAll();
    // }

    // public void deleteValidation(ValidationTransaction validation) {
    // validationRepo.delete(validation);
    // }
}
