package com.example.service;

import com.example.change_dtos.DeviseDto;
import com.example.mappers.VirementMapper;
import com.example.models.CompteCourant;
import com.example.models.Etat;
import com.example.models.Frais;
import com.example.models.HistoriqueVirement;
import com.example.models.TransactionCourant;
import com.example.models.ValidationVirement;
import com.example.models.Virement;
import com.example.remotes.ValidationVirementServiceRemote;
import com.example.remotes.VirementServiceRemote;
import com.example.repositories.CompteCourantRepository;
import com.example.repositories.EtatRepository;
import com.example.repositories.FraisRepository;
import com.example.repositories.HistoriqueVirementRepository;
import com.example.repositories.ValidationVirementRepository;
import com.example.repositories.VirementRepository;
import com.example.server_dtos.VirementDto;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class VirementService implements VirementServiceRemote {

    @EJB
    private VirementRepository virementRepo;

    @EJB
    private CompteCourantRepository compteCourantRepository;

    @EJB
    private EtatRepository etatRepository;

    @EJB
    private ValidationVirementRepository validationVirementRepository;

    @EJB
    private HistoriqueVirementRepository historiqueVirementRepository;

    @EJB
    private FraisRepository fraisRepository;

    @EJB
    private ValidationVirementServiceRemote validationVirementServiceRemote;

    @Resource
    private EJBContext ejbContext;

    // @Override
    // public boolean denyAllOperations(DeviseDto deviseDto) {
    // try {
    // if (deviseDto == null || deviseDto.getLibelle() == null)
    // throw new Exception("La devise est invalide");

    // LocalDateTime infDate = deviseDto.getDateDebutDate();
    // LocalDateTime supDate = deviseDto.getDateFinDate(); // peut être null → pas
    // de borne supérieure

    // List<Virement> virements = virementRepo.findByDevise(deviseDto.getLibelle());
    // boolean anyDenied = false;

    // for (Virement virement : virements) {
    // LocalDateTime transDate = virement.getDateVirement();

    // boolean isBetween = (transDate.isAfter(infDate) ||
    // transDate.isEqual(infDate))
    // && (supDate == null || transDate.isBefore(supDate) ||
    // transDate.isEqual(supDate));

    // // if (isBetween) {
    // // validationVirementServiceRemote.saveValidation(
    // // virement.getIdVirement(),
    // // null,
    // // "annuler",
    // // false);
    // // anyDenied = true;
    // // }
    // }

    // return anyDenied;

    // } catch (Exception e) {
    // // ejbContext.setRollbackOnly();
    // e.printStackTrace();
    // return false;
    // // throw e;
    // }

    // }
    @Override
    public double alleas(VirementDto vdDto) throws Exception {
        // CompteCourant debit = compteCourantRepository.findById(vdDto.getIdCompteDebit());
        // CompteCourant credit = compteCourantRepository.findById(vdDto.getIdCompteCredit());

        // Virement virement = VirementMapper.toEntity(vdDto, debit, credit);
        Frais frais = null;
        List<Frais> fraisss  =  fraisRepository.findAll();
        if (fraisss == null || fraisss.isEmpty()) {
            throw new Exception("Aucun frais disponible pour ce type de compte");
        }

        double montant = vdDto.getMontant();
        // double montantFinal = montant;

        for (Frais f : fraisss) {
            Double montantInf = f.getMontantInf();
            Double montantSup = f.getMontantSup();
            // Double fondPourcentage = f.getFondPourcentage();
            // Double fondMontant = f.getFondMontant();

            if (montantSup == null)
                montantSup = Double.MAX_VALUE;

            if (montant >= montantInf && montant <= montantSup) {
                frais = f;
            }
        }


        return frais.getFraisValue(vdDto.getMontant());
    }

    @Override
    public VirementDto effectuerVirement(VirementDto virementDto, Double tauxChange) throws Exception {

        try {
            CompteCourant compteDebit = compteCourantRepository.findById(virementDto.getIdCompteDebit());
            CompteCourant compteCredit = compteCourantRepository.findById(virementDto.getIdCompteCredit());

            List<Frais> frais = fraisRepository.findByTypeCompteId(compteCredit.getTypeCompte().getIdTypeCompte());
            Virement virement = compteDebit.virer(compteCredit, tauxChange, frais, virementDto.getDateVirement(),
                    virementDto.getMontant(),
                    virementDto.getDevise());

            virementRepo.create(virement);

            ValidationVirement validationVirement = new ValidationVirement();
            Etat etat = etatRepository.findByLibelle("en_attente");

            validationVirement.setDateValidation(virementDto.getDateVirement());
            validationVirement.setEtat(etat);
            validationVirement.setVirement(virement);

            HistoriqueVirement hist = virement.generateHistorique(etat, null, "creation");

            validationVirementRepository.create(validationVirement);
            historiqueVirementRepository.create(hist);

            return VirementMapper.toDto(virement);

        } catch (Exception e) {
            ejbContext.setRollbackOnly();
            throw e;
        }

    }

    public List<Virement> getAllVirements() {
        return virementRepo.findAll();
    }

    public Virement getVirementById(Integer id) {
        return virementRepo.findById(id);
    }

    @Override
    public List<VirementDto> getVirementsByCompteDebitAndCompteCredit(Integer idCompte) throws Exception {
        try {
            List<VirementDto> resp = new ArrayList<>();

            for (Virement virement : virementRepo.findByCompteDebitOrCredit(idCompte)) {
                resp.add(VirementMapper.toDto(virement));
            }
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public List<Virement> getVirementsByCompteCredit(Integer idCompte) {
        return virementRepo.findByCompteCredit(idCompte);
    }

    public Virement updateVirement(Virement virement) {
        return virementRepo.update(virement);
    }

    public void deleteVirement(Virement virement) {
        virementRepo.delete(virement);
    }
}
