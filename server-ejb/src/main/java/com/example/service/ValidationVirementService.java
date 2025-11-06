package com.example.service;

import com.example.mappers.VirementMapper;
import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.models.Etat;
import com.example.models.HistoriqueVirement;
import com.example.models.TransactionCourant;
import com.example.models.ValidationTransaction;
import com.example.models.ValidationVirement;
import com.example.models.Virement;
import com.example.remotes.ValidationServiceRemote;
import com.example.remotes.ValidationTransactionServiceRemote;
import com.example.remotes.ValidationVirementServiceRemote;
import com.example.repositories.ClientCourantRepository;
import com.example.repositories.CompteCourantRepository;
import com.example.repositories.EtatRepository;
import com.example.repositories.HistoriqueVirementRepository;
import com.example.repositories.TransactionCourantRepository;
import com.example.repositories.ValidationVirementRepository;
import com.example.repositories.VirementRepository;
import com.example.server_dtos.VirementDto;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;

@Stateless
public class ValidationVirementService implements ValidationVirementServiceRemote {

    @EJB
    private ValidationVirementRepository validationVirementRepository;

    @EJB
    private VirementRepository virementRepository;

    @EJB
    private ClientCourantRepository clientCourantRepository;

    @EJB
    private CompteCourantRepository compteCourantRepository;

    @EJB
    private TransactionCourantRepository transactionCourantRepository;

    @EJB
    private EtatRepository etatRepository;

    @EJB
    private HistoriqueVirementRepository historiqueVirementRepository;

    @Resource
    private EJBContext ejbContext;

    // service
    @EJB
    private ValidationTransactionServiceRemote validationTransactionServiceRemote;

    @Override
    public boolean saveValidation(Integer idVirement, Double newMontant, Double newTaux , Integer idUtilisateur, String action,
            boolean doubleChangeControl)
            throws Exception {
        try {

            Virement virement = virementRepository.findById(idVirement);
            Etat etat = etatRepository.findByLibelle(action);
            if (idUtilisateur == null) {
                throw new Exception("L'ID de l'utilisateur est requis pour valider ou annuler un virement");
            }
            if (virement == null) {
                throw new Exception("Virement introuvable avec id=" + idVirement);
            }
            ClientCourant utilisateur = clientCourantRepository.findById(idUtilisateur);

            ValidationVirement validationVirement = virement.changeEtat(etat, utilisateur, doubleChangeControl);

            validationVirementRepository.create(validationVirement);

            CompteCourant compteDebit = virement.getCompteDebit();
            CompteCourant compteCredit = virement.getCompteCredit();

            // // compte de la banque
            CompteCourant compteBanque = compteCourantRepository.findById(3);
            if (compteBanque == null)
                throw new Exception("Compte de la banque est null");
            TransactionCourant banqueTrans = compteBanque.makeTransactionByVirement(virement);

            if (newMontant != null) 
            {
                virement.setMontant(newMontant);
                virement.setTaux(newTaux);
                virementRepository.update(virement);

            }

            TransactionCourant debitTrans = compteDebit.makeTransactionByVirement(virement);
            TransactionCourant creditTrans = compteCredit.makeTransactionByVirement(virement);

            // // TransactionCourant creditTrans =
            // compteCredit.makeTransactionByVirement(virement);

            HistoriqueVirement hist = virement.generateHistorique(etat, utilisateur, "annulation");
            historiqueVirementRepository.create(hist);

            // historiqueVirementRepository.create(hist);
            // ValidationTransaction debitValid = debitTrans.changeEtat(etat, false);
            // ValidationTransaction creditValid = debitTrans.changeEtat(etat, false);

            if (action.equals("valider")) {
                hist = virement.generateHistorique(etat, utilisateur, "validation");

                historiqueVirementRepository.create(hist);
                
                compteDebit.controlVirement(virement.getMontant());
                transactionCourantRepository.create(debitTrans);
                transactionCourantRepository.create(creditTrans);

                transactionCourantRepository.create(banqueTrans);

                System.out.println("tayyyyyy1 " + debitTrans.getIdTransaction());

                System.out.println("tayyyyyy " + creditTrans.getIdTransaction());

                validationTransactionServiceRemote.saveValidation(debitTrans.getIdTransaction(), null, "valider",
                        false);

                validationTransactionServiceRemote.saveValidation(creditTrans.getIdTransaction(), null, "valider",
                        false);

                validationTransactionServiceRemote.saveValidation(banqueTrans.getIdTransaction(), null, "valider",
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
