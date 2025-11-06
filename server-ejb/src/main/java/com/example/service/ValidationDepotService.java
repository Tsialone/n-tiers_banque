package com.example.service;

import java.time.LocalDateTime;

import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.models.Depot;
import com.example.models.Etat;
import com.example.models.HistoriqueDepot;
import com.example.models.HistoriqueVirement;
import com.example.models.TransactionCourant;
import com.example.models.ValidationDepot;
import com.example.remotes.ValidationDepotServiceRemote;
import com.example.remotes.ValidationTransactionServiceRemote;
import com.example.repositories.ClientCourantRepository;
import com.example.repositories.DepotRepository;
import com.example.repositories.EtatRepository;
import com.example.repositories.HistoriqueDepotRepository;
import com.example.repositories.TransactionCourantRepository;
import com.example.repositories.ValidationDepotRepository;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;

@Stateless
public class ValidationDepotService implements ValidationDepotServiceRemote {

    @EJB
    private ValidationDepotRepository validationDepotRepository;

    @EJB
    private DepotRepository depotRepository;

    @EJB
    private EtatRepository etatRepository;

    @EJB
    private ClientCourantRepository clientCourantRepository;

    @EJB
    private ValidationTransactionServiceRemote validationTransactionServiceRemote;

    @EJB
    private TransactionCourantRepository transactionCourantRepository;

    @EJB
    private HistoriqueDepotRepository historiqueDepotRepository;

    @Resource
    private EJBContext ejbContext;

    @Override
    public boolean saveValidation(Integer idDepot, Double newMontant,  Double newTaux , String action, boolean doubleChangeControl)
            throws Exception {
        try {
            if (idDepot == null)
                throw new Exception("L'id du dépôt est requis.");
            if (action == null || action.isEmpty())
                throw new Exception("L'action est requise.");

            Depot depot = depotRepository.findById(idDepot);
            if (depot == null)
                throw new Exception("Dépôt introuvable avec id = " + idDepot);

            Etat etat = etatRepository.findByLibelle(action);
            if (etat == null)
                throw new Exception("État introuvable pour l'action : " + action);
            // Créer la validation
            ValidationDepot validation = depot.changeEtat(etat, null, doubleChangeControl);
            validationDepotRepository.create(validation);

            if (newMontant != null && newTaux != null) {
                depot.setMontant(newMontant);
                depot.setTaux(newTaux);
                depotRepository.update(depot);
            }

            HistoriqueDepot hist = depot.generateHistorique(etat, null, action);
            historiqueDepotRepository.create(hist);

            if (action.equals("valider")) {
                // hist = depot.generateHistorique(etat, null, "valider");
                // historiqueDepotRepository.create(hist);

                CompteCourant compteCredit = depot.getCompteCredit();
                TransactionCourant depotTrans = compteCredit.makeTransactionByDepot(depot);

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
