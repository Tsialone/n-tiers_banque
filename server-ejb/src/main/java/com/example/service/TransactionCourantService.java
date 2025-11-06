package com.example.service;

import com.example.models.CompteCourant;
import com.example.models.Depot;
import com.example.models.Retrait;
import com.example.models.TransactionCourant;
import com.example.models.Virement;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.remotes.TransactionCourantServiceRemote;
import com.example.remotes.ValidationTransactionServiceRemote;
import com.example.repositories.CompteCourantRepository;
import com.example.repositories.DepotRepository;
import com.example.repositories.RetraitRepository;
import com.example.repositories.TransactionCourantRepository;
import com.example.repositories.VirementRepository;
import com.example.server_dtos.CompteCourantDto;
import com.example.server_dtos.TransactionCourantDto;
import com.example.server_dtos.VirementDto;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.change_dtos.DeviseDto;
import com.example.mappers.TransactionCourantMapper;

@Stateless
public class TransactionCourantService implements TransactionCourantServiceRemote {

    @EJB
    private TransactionCourantRepository repository;

    @EJB
    private CompteCourantRepository compteCourantRepository;

    @EJB
    private CompteCourantServiceRemote compteCourantServiceRemote;

    @EJB
    private VirementRepository virementRepository;

    @EJB
    private DepotRepository depotRepository;

    @EJB
    private RetraitRepository retraitRepository;

    @EJB
    private ValidationTransactionServiceRemote validationTransactionServiceRemote;

    // @EJB
    // private ValidationVirementService validationVirementService;

    @Resource
    private EJBContext ejbContext;

    @Override
    public List<TransactionCourantDto> getAllByIdCompte(Integer idCompteCourant) {
        CompteCourant compteCourant = compteCourantRepository.findById(idCompteCourant);
        if (compteCourant == null)
            return new ArrayList<>();

        List<TransactionCourant> transactionCourants = new ArrayList<>();
        List<Virement> allVirement = virementRepository.findAll();
        List<Depot> depots = depotRepository.findByCompte(idCompteCourant);
        List<Retrait> retraits = retraitRepository.findByCompte(idCompteCourant);

        if (compteCourant.getNom().equals("banque")) {
            for (Virement virement : allVirement) {
                List<TransactionCourant> txs = repository.findBySource(virement.getIdObject());
                for (TransactionCourant tx : txs) {
                    if (tx.isBanque()) {
                        transactionCourants.add(tx);
                    }
                }
            }
        }

        // Tous les virements émis
        for (Virement virement : compteCourant.getVirementsEmis()) {
            List<TransactionCourant> txs = repository.findBySource(virement.getIdObject());
            for (TransactionCourant tx : txs) {
                if ("debit".equalsIgnoreCase(tx.getSens()) &&
                        virement.getCompteDebit().getIdCompte().equals(idCompteCourant)) {
                    transactionCourants.add(tx);
                }
            }
        }

        // Tous les virements reçus
        for (Virement virement : compteCourant.getVirementsRecus()) {
            List<TransactionCourant> txs = repository.findBySource(virement.getIdObject());
            for (TransactionCourant tx : txs) {
                if ("credit".equalsIgnoreCase(tx.getSens()) &&
                        virement.getCompteCredit().getIdCompte().equals(idCompteCourant)) {
                    transactionCourants.add(tx);
                }
            }
        }

        for (Depot depot : depots) {
            List<TransactionCourant> txs = repository.findBySource(depot.getIdObject());
            transactionCourants.addAll(txs);
        }

        for (Retrait retrait : retraits) {
            List<TransactionCourant> txs = repository.findBySource(retrait.getIdObject());
            transactionCourants.addAll(txs);
        }
        // Trier par date_transaction
        // transactionCourants.sort(Comparator.comparing(TransactionCourant::getDateTransaction));

        return transactionCourants.stream()
                .map(TransactionCourantMapper::toDto)
                .collect(Collectors.toList());
    }

    // public boolean isBetween (){

    // }

    @Override
    public boolean denyAllOperations(DeviseDto deviseDto, Integer idUtilisateur) throws Exception {
        try {
            if (deviseDto == null || deviseDto.getLibelle() == null)
                throw new Exception("La devise est invalide");

            LocalDateTime infDate = deviseDto.getDateDebutDate();
            LocalDateTime supDate = deviseDto.getDateFinDate(); // peut être null → pas de borne supérieure

            List<TransactionCourant> transactions = repository.findByDevise(deviseDto.getLibelle());
            boolean anyDenied = false;

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

                    // Virement v = transaction.getVirement();
                    // validationVirementService.saveValidation(v.getIdVirement(), v.getMontant() /
                    // deviseDto.getArriary(),
                    // idUtilisateur, "valider", false);
                    // anyDenied = true;

                }

            }

            return anyDenied;

        } catch (Exception e) {
            ejbContext.setRollbackOnly();
            e.printStackTrace();
            throw e;
        }

    }
    // @Override
    // public VirementDto doVirement(VirementDto virementDto) throws Exception {

    // CompteCourantMapper compteDebit =
    // compteCourantRepository.findById(virementDto.getIdCompteDebit());
    // CompteCourantMapper compteCredit =
    // compteCourantRepository.findById(virementDto.getIdCompteCredit());
    // // List<CompteCourant> compteCourants =
    // //
    // compteCourantRepository.findByClientId(compteDebit.getClient().getIdClient());

    // // for (CompteCourant compteCourant : compteCourants) {
    // // if (compteCourant.getIdCompte().equals(compteCredit.getIdCompte())){
    // // }
    // // }

    // Virement virement = compteDebit.virer(compteCredit,
    // virementDto.getDateVirement(), virementDto.getMontant(),
    // virementDto.getDevise());

    // TransactionCourantDto transactionCourantDtoDebiteur = new
    // TransactionCourantDto();

    // // mv pour debiteur
    // transactionCourantDtoDebiteur.setDateTransaction(virement.getDateVirement());
    // transactionCourantDtoDebiteur.setDevise(virement.getDevise());
    // transactionCourantDtoDebiteur.setIdCompte(virement.getCompteDebit().getIdCompte());
    // transactionCourantDtoDebiteur.setIdCompteDest(virement.getCompteCredit().getIdCompte());
    // transactionCourantDtoDebiteur.setMontant(virement.getMontant());
    // transactionCourantDtoDebiteur.setSens("debit");
    // transactionCourantDtoDebiteur.setLibelle("virement à " +
    // compteCredit.getIdCompte());

    // TransactionCourantDto transactionCourantDtoCrediteur = new
    // TransactionCourantDto();
    // // mv pour crediteur
    // transactionCourantDtoCrediteur.setDateTransaction(virement.getDateVirement());
    // transactionCourantDtoCrediteur.setDevise(virement.getDevise());
    // transactionCourantDtoCrediteur.setIdCompte(virement.getCompteCredit().getIdCompte());
    // transactionCourantDtoCrediteur.setIdCompteDest(null);
    // transactionCourantDtoCrediteur.setMontant(virement.getMontant());
    // transactionCourantDtoCrediteur.setSens("credit");
    // transactionCourantDtoCrediteur.setLibelle("virement de " +
    // compteDebit.getIdCompte());

    // TransactionCourant transactionCourantCrediteur = TransactionCourantMapper
    // .toEntity(transactionCourantDtoCrediteur, compteCredit, null);
    // TransactionCourant transactionCourantDebiteur =
    // TransactionCourantMapper.toEntity(transactionCourantDtoDebiteur,
    // compteDebit, compteCredit);

    // Integer newIdVirement = virementService.getNewId();
    // transactionCourantCrediteur.setIdVirement(newIdVirement);
    // transactionCourantDebiteur.setIdVirement(newIdVirement);

    // repository.save(transactionCourantCrediteur);
    // // throw new Exception("test kely masiso");
    // repository.save(transactionCourantDebiteur);

    // return new VirementDto();
    // }

    // @Override
    // public TransactionCourantDto updateTransactionCourant(TransactionCourantDto
    // transactionCourantDto) {
    // // CompteCourantDto compteCourant =
    // //
    // compteCourantServiceRemote.getCompteById(transactionCourantDto.getIdCompte());
    // CompteCourantMapper compteCourant =
    // compteCourantRepository.findById(transactionCourantDto.getIdCompte());
    // TransactionCourant transactionCourant =
    // TransactionCourantMapper.toEntity(transactionCourantDto, compteCourant,
    // null);
    // System.out.println("transaction courant a mettre a jour " +
    // transactionCourant);

    // return TransactionCourantMapper.toDto(repository.update(transactionCourant));
    // }

    // @Override
    // public List<TransactionCourantDto> getAllTransactionsByClientAndCourant(int
    // idClient, int idCourant) {
    // List<TransactionCourantDto> transactionsCourantDto = new ArrayList<>();
    // for (TransactionCourant transactionCourant : repository.getAll()) {
    // if (transactionCourant.getCompte().getClient().getIdClient().equals(idClient)
    // && transactionCourant.getCompte().getIdCompte().equals(idCourant)) {
    // transactionsCourantDto.add(TransactionCourantMapper.toDto(transactionCourant));
    // }
    // }
    // return transactionsCourantDto;
    // }

    // @Override
    // public List<TransactionCourantDto> getAllTransactionsByClient(int idClient) {
    // List<TransactionCourantDto> transactionsCourantDto = new ArrayList<>();
    // for (TransactionCourant transactionCourant : repository.getAll()) {
    // if
    // (transactionCourant.getCompte().getClient().getIdClient().equals(idClient)) {
    // transactionsCourantDto.add(TransactionCourantMapper.toDto(transactionCourant));
    // }
    // }
    // return transactionsCourantDto;
    // }

    @Override
    public List<TransactionCourantDto> getAllTransactions() {
        List<TransactionCourantDto> transactionsCourantDto = new ArrayList<>();
        for (TransactionCourant transactionCourant : repository.findAll()) {
            transactionsCourantDto.add(TransactionCourantMapper.toDto(transactionCourant));
        }
        return transactionsCourantDto;
    }

    // Récupérer une transaction par ID
    // @Override
    // public TransactionCourantDto getTransactionById(Integer id) {
    // TransactionCourant t = repository.findById(id);
    // if (t == null) {
    // throw new IllegalArgumentException("Transaction non trouvée : " + id);
    // }
    // return TransactionCourantMapper.toDto(t);
    // }

    // Récupérer toutes les transactions d'un compte
    // @Override
    // public List<TransactionCourantDto> getTransactionsByCompte(Integer idCompte)
    // {
    // return repository.findByCompteId(idCompte)
    // .stream()
    // .map(TransactionCourantMapper::toDto)
    // .collect(Collectors.toList());
    // }

    // Ajouter ou mettre à jour une transaction
    @Override
    public TransactionCourantDto saveTransaction(TransactionCourantDto transaction) {
        // CompteCourantMapper compteCourant =
        // compteCourantRepository.findById(transaction.getIdCompte());
        TransactionCourant transactionCourant = TransactionCourantMapper.toEntity(transaction);
        repository.create(transactionCourant);
        return transaction;
    }

    // Supprimer une transaction
    // public void deleteTransaction(TransactionCourantDto transaction) {
    // TransactionCourant transactionCourant =
    // repository.findById(transaction.getIdTransaction());
    // repository.delete(transactionCourant);
    // }
}
