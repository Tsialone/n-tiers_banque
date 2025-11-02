package com.example.service;

import com.example.models.CompteCourant;
import com.example.models.TransactionCourant;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.remotes.TransactionCourantServiceRemote;
import com.example.repositories.CompteCourantRepository;
import com.example.repositories.TransactionCourantRepository;
import com.example.server_dtos.CompteCourantDto;
import com.example.server_dtos.TransactionCourantDto;
import com.example.server_dtos.VirementDto;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.change_dtos.DeviseDto;
import com.example.classes.Virement;
import com.example.mappers.TransactionCourantMapper;
import com.example.mappers.VirementMapper;

@Stateless
public class TransactionCourantService implements TransactionCourantServiceRemote {

    @EJB
    private TransactionCourantRepository repository;

    @EJB
    private CompteCourantRepository compteCourantRepository;

    @EJB
    private VirementService virementService;

    @EJB
    private CompteCourantServiceRemote compteCourantServiceRemote;


    @Override
    public boolean updateAllOperations(DeviseDto deviseDto) {

        List<TransactionCourant> allTransactionCourants = repository.getAll();
        for (TransactionCourant transactionCourant : allTransactionCourants) {
            transactionCourant.updateByDevise(deviseDto);
            repository.update(transactionCourant);
        }
        return false;
    }

    @Override
    public VirementDto doVirement(VirementDto virementDto) throws Exception {

        CompteCourant compteDebit = compteCourantRepository.findById(virementDto.getIdCompteDebit());
        CompteCourant compteCredit = compteCourantRepository.findById(virementDto.getIdCompteCredit());
        // List<CompteCourant> compteCourants =
        // compteCourantRepository.findByClientId(compteDebit.getClient().getIdClient());

        // for (CompteCourant compteCourant : compteCourants) {
        // if (compteCourant.getIdCompte().equals(compteCredit.getIdCompte())){
        // }
        // }

        Virement virement = compteDebit.virer(compteCredit, virementDto.getDateVirement(), virementDto.getMontant(),
                virementDto.getDevise());

        TransactionCourantDto transactionCourantDtoDebiteur = new TransactionCourantDto();

        // mv pour debiteur
        transactionCourantDtoDebiteur.setDateTransaction(virement.getDateVirement());
        transactionCourantDtoDebiteur.setDevise(virement.getDevise());
        transactionCourantDtoDebiteur.setIdCompte(virement.getCompteDebit().getIdCompte());
        transactionCourantDtoDebiteur.setIdCompteDest(virement.getCompteCredit().getIdCompte());
        transactionCourantDtoDebiteur.setMontant(virement.getMontant());
        transactionCourantDtoDebiteur.setSens("debit");
        transactionCourantDtoDebiteur.setLibelle("virement à " + compteCredit.getIdCompte());

        TransactionCourantDto transactionCourantDtoCrediteur = new TransactionCourantDto();
        // mv pour crediteur
        transactionCourantDtoCrediteur.setDateTransaction(virement.getDateVirement());
        transactionCourantDtoCrediteur.setDevise(virement.getDevise());
        transactionCourantDtoCrediteur.setIdCompte(virement.getCompteCredit().getIdCompte());
        transactionCourantDtoCrediteur.setIdCompteDest(null);
        transactionCourantDtoCrediteur.setMontant(virement.getMontant());
        transactionCourantDtoCrediteur.setSens("credit");
        transactionCourantDtoCrediteur.setLibelle("virement de " + compteDebit.getIdCompte());

        TransactionCourant transactionCourantCrediteur = TransactionCourantMapper
                .toEntity(transactionCourantDtoCrediteur, compteCredit, null);
        TransactionCourant transactionCourantDebiteur = TransactionCourantMapper.toEntity(transactionCourantDtoDebiteur,
                compteDebit, compteCredit);

        Integer newIdVirement = virementService.getNewId();
        transactionCourantCrediteur.setIdVirement(newIdVirement);
        transactionCourantDebiteur.setIdVirement(newIdVirement);

        repository.save(transactionCourantCrediteur);
        repository.save(transactionCourantDebiteur);

        return VirementMapper.toDto(virement);
    }

    @Override
    public TransactionCourantDto updateTransactionCourant(TransactionCourantDto transactionCourantDto) {
        // CompteCourantDto compteCourant =
        // compteCourantServiceRemote.getCompteById(transactionCourantDto.getIdCompte());
        CompteCourant compteCourant = compteCourantRepository.findById(transactionCourantDto.getIdCompte());
        TransactionCourant transactionCourant = TransactionCourantMapper.toEntity(transactionCourantDto, compteCourant,
                null);
        System.out.println("transaction courant a mettre a jour " + transactionCourant);

        return TransactionCourantMapper.toDto(repository.update(transactionCourant));
    }

    @Override
    public List<TransactionCourantDto> getAllTransactionsByClientAndCourant(int idClient, int idCourant) {
        List<TransactionCourantDto> transactionsCourantDto = new ArrayList<>();
        for (TransactionCourant transactionCourant : repository.getAll()) {
            if (transactionCourant.getCompte().getClient().getIdClient().equals(idClient)
                    && transactionCourant.getCompte().getIdCompte().equals(idCourant)) {
                transactionsCourantDto.add(TransactionCourantMapper.toDto(transactionCourant));
            }
        }
        return transactionsCourantDto;
    }

    @Override
    public List<TransactionCourantDto> getAllTransactionsByClient(int idClient) {
        List<TransactionCourantDto> transactionsCourantDto = new ArrayList<>();
        for (TransactionCourant transactionCourant : repository.getAll()) {
            if (transactionCourant.getCompte().getClient().getIdClient().equals(idClient)) {
                transactionsCourantDto.add(TransactionCourantMapper.toDto(transactionCourant));
            }
        }
        return transactionsCourantDto;
    }

    @Override
    public List<TransactionCourantDto> getAllTransactions() {
        List<TransactionCourantDto> transactionsCourantDto = new ArrayList<>();
        for (TransactionCourant transactionCourant : repository.getAll()) {
            transactionsCourantDto.add(TransactionCourantMapper.toDto(transactionCourant));
        }
        return transactionsCourantDto;
    }

    // Récupérer une transaction par ID
    @Override
    public TransactionCourantDto getTransactionById(Integer id) {
        TransactionCourant t = repository.findById(id);
        if (t == null) {
            throw new IllegalArgumentException("Transaction non trouvée : " + id);
        }
        return TransactionCourantMapper.toDto(t);
    }

    // Récupérer toutes les transactions d'un compte
    @Override
    public List<TransactionCourantDto> getTransactionsByCompte(Integer idCompte) {
        return repository.findByCompteId(idCompte)
                .stream()
                .map(TransactionCourantMapper::toDto)
                .collect(Collectors.toList());
    }

    // Ajouter ou mettre à jour une transaction
    @Override
    public TransactionCourantDto saveTransaction(TransactionCourantDto transaction) {
        CompteCourant compteCourant = compteCourantRepository.findById(transaction.getIdCompte());
        TransactionCourant transactionCourant = TransactionCourantMapper.toEntity(transaction, compteCourant, null);
        repository.save(transactionCourant);
        return transaction;
    }

    // Supprimer une transaction
    public void deleteTransaction(TransactionCourantDto transaction) {
        TransactionCourant transactionCourant = repository.findById(transaction.getIdTransaction());
        repository.delete(transactionCourant);
    }
}
