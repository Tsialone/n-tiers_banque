package com.example.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.change_dtos.DeviseDto;
import com.example.models.Depot;
import com.example.models.HistoriqueDepot;
import com.example.models.HistoriqueRetrait;
import com.example.models.HistoriqueVirement;
import com.example.models.Retrait;
import com.example.models.TransactionCourant;
import com.example.models.Virement;
import com.example.remotes.DenyServiceRemote;
import com.example.remotes.ValidationDepotServiceRemote;
import com.example.remotes.ValidationRetraitServiceRemote;
import com.example.remotes.ValidationTransactionServiceRemote;
import com.example.remotes.ValidationVirementServiceRemote;
import com.example.repositories.DepotRepository;
import com.example.repositories.RetraitRepository;
import com.example.repositories.TransactionCourantRepository;
import com.example.repositories.VirementRepository;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;

@Stateless
public class DenyService implements DenyServiceRemote {

    @EJB
    private TransactionCourantRepository repository;

    @EJB
    private ValidationTransactionServiceRemote validationTransactionServiceRemote;

    @EJB
    private ValidationVirementServiceRemote validationVirementServiceRemote;

    @EJB
    private DepotRepository depotRepository;
    @EJB
    private ValidationRetraitServiceRemote validationRetraitServiceRemote;

    @EJB
    private ValidationDepotServiceRemote validationDepotServiceRemote;
    @EJB
    private VirementRepository virementRepository;

      @EJB
    private RetraitRepository retraitRepository;

    @Resource
    private EJBContext ejbContext;

    @Override
    public boolean denyAllOperations(DeviseDto deviseDto, Integer idUtilisateur) throws Exception {
        try {
            if (deviseDto == null || deviseDto.getLibelle() == null)
                throw new Exception("La devise est invalide");

            LocalDateTime infDate = deviseDto.getDateDebutDate();
            LocalDateTime supDate = deviseDto.getDateFinDate(); // peut être null → pas de borne supérieure

            List<TransactionCourant> transactions = repository.findByDevise(deviseDto.getLibelle());
            boolean anyDenied = false;
            List<String> bySource = new ArrayList<>();
            for (TransactionCourant transaction : transactions) {
                LocalDateTime transDate = transaction.getDateTransaction().atStartOfDay();
                boolean isBetween = (transDate.isAfter(infDate) || transDate.isEqual(infDate))
                        && (supDate == null || transDate.isBefore(supDate) || transDate.isEqual(supDate));

                if (isBetween) {

                    validationTransactionServiceRemote.saveValidation(
                            transaction.getIdTransaction(),
                            idUtilisateur,
                            "annuler",
                            false);
                    Virement v = transaction.getVirement(virementRepository);
                    Depot d = transaction.getDepot(depotRepository);
                    Retrait r = transaction.getRetrait(retraitRepository);
                    Double newTaux = deviseDto.getArriary();


                    if (!bySource.contains(transaction.getSource())) {
                        // double newSolde = 0;

                        if (v != null) {
                            HistoriqueVirement lastHisto = v.getLastHistorique();
                            double newSolde = v.getMontant() * deviseDto.getArriary() / lastHisto.getTaux();

                            validationVirementServiceRemote.saveValidation(v.getIdVirement(), newSolde, newTaux ,
                                    idUtilisateur, "en_attente", false);
                        }
                        if (d != null) {
                            HistoriqueDepot lastDepotHist = d.getLastHistorique();
                            double depotNewSolde = d.getMontant() * deviseDto.getArriary() / lastDepotHist.getTaux();
                            validationDepotServiceRemote.saveValidation(d.getIdDepot(), depotNewSolde, newTaux ,
                                    "en_attente", false);
                        }
                          if (r != null) {
                            HistoriqueRetrait lastRetraiHist = r.getLastHistorique();
                            double retraitNewSolde = r.getMontant() * deviseDto.getArriary() / lastRetraiHist.getTaux();
                            validationRetraitServiceRemote.saveValidation(r.getIdRetrait(), retraitNewSolde, newTaux ,
                                    "en_attente", false);
                        }
                        anyDenied = true;
                    }

                    bySource.add(transaction.getSource());

                } 
                // else {
                //     System.out.println("tayyyyyyyyyyy: " + transaction.getIdTransaction());
                //     System.out.println("ito ee: " + transaction.getDateTransaction() + " inf: " + infDate + " sup:"  + supDate ) ;
                // }

            }

            return anyDenied;

        } catch (Exception e) {
            ejbContext.setRollbackOnly();
            e.printStackTrace();
            throw e;
        }

    }
}
