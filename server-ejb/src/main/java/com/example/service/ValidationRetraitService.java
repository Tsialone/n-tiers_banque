package com.example.service;

import com.example.models.*;
import com.example.repositories.*;
import com.example.remotes.TransactionCourantServiceRemote;
import com.example.remotes.ValidationRetraitServiceRemote;
import com.example.remotes.ValidationTransactionServiceRemote;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;

@Stateless
public class ValidationRetraitService implements ValidationRetraitServiceRemote {

    @EJB
    private RetraitRepository retraitRepository;

    @EJB
    private ValidationRetraitRepository validationRetraitRepository;
    @EJB
    private TransactionCourantRepository transactionCourantRepository;

    @EJB
    private ValidationTransactionServiceRemote validationTransactionServiceRemote;
    @EJB
    private EtatRepository etatRepository;

    @EJB
    private HistoriqueRetraitRepository historiqueRetraitRepository;

    @Resource
    private EJBContext ejbContext;

    @Override
    public boolean saveValidation(Integer idRetrait, Double newMontant, Double newTaux, String action, boolean doubleChangeControl)
            throws Exception {
        try {
            if (idRetrait == null)
                throw new Exception("L'id du retrait est requis.");
            if (action == null || action.isEmpty())
                throw new Exception("L'action est requise.");

            Retrait retrait = retraitRepository.findById(idRetrait);
            if (retrait == null)
                throw new Exception("Retrait introuvable avec id = " + idRetrait);

            Etat etat = etatRepository.findByLibelle(action);
            if (etat == null)
                throw new Exception("État introuvable pour l'action : " + action);

            // Créer la validation
            ValidationRetrait validation = retrait.changeEtat(etat, null, doubleChangeControl);
            validationRetraitRepository.create(validation);

            if (newMontant != null) {
                retrait.setMontant(newMontant);
                retrait.setTaux(newTaux);
                retraitRepository.update(retrait);
            }

            HistoriqueRetrait hist = retrait.generateHistorique(etat, null, action);
            historiqueRetraitRepository.create(hist);

            if (action.equals("valider")) {
                CompteCourant compteDebit = retrait.getCompteDebit();
                TransactionCourant depotTrans = compteDebit.makeTransactionByRetrait(retrait);

                transactionCourantRepository.create(depotTrans);
                validationTransactionServiceRemote.saveValidation(depotTrans.getIdTransaction(), null, "valider",
                        false);
            }

            return true;
        } catch (Exception e) {
            ejbContext.setRollbackOnly();
            e.printStackTrace();
            throw e;
        }
    }
}
