package com.example.service;

import com.example.mappers.DepotMapper;
import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.models.Depot;
import com.example.models.Etat;
import com.example.models.HistoriqueDepot;
import com.example.remotes.DepotServiceRemote;
import com.example.repositories.ClientCourantRepository;
import com.example.repositories.CompteCourantRepository;
import com.example.repositories.DepotRepository;
import com.example.repositories.EtatRepository;
import com.example.repositories.HistoriqueDepotRepository;
import com.example.server_dtos.DepotDto;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class DepotService implements DepotServiceRemote {

    @EJB
    private DepotRepository repository;

    @EJB
    private HistoriqueDepotRepository historiqueDepotRepository;

    @EJB
    private EtatRepository etatRepository;


    @EJB
    private CompteCourantRepository compteCourantRepository;
    @EJB
    private ClientCourantRepository clientCourantRepository;

    @Resource
    private EJBContext ejbContext;
    // @Override
    // public Depot findById(Integer id) throws Exception {
    // Depot depot = repository.findById(id);
    // if (depot == null) {
    // throw new Exception("Dépôt introuvable avec l'id : " + id);
    // }
    // return depot;
    // }

    // @Override
    // public List<Depot> findAll() throws Exception {
    // return repository.findAll();
    // }

    @Override
    public void create(DepotDto depot) throws Exception {

        try {
            if (depot == null) {
                throw new Exception("Le dépôt ne doit pas être null");
            }
            // ClientCourant clientCourant = clientCourantRepository.findById(1);
            CompteCourant compteDebit = compteCourantRepository.findById(depot.getIdCompteCredit());

            Depot depotEntity = DepotMapper.toEntity(depot);
            compteDebit.controlDepot(depotEntity.getMontant());
            Etat etat = etatRepository.findByLibelle("en_attente");
            repository.create(depotEntity);
            // repository.f;
            HistoriqueDepot hist = depotEntity.generateHistorique(etat, null, "creation");
            historiqueDepotRepository.create(hist);
            // throw new Exception(hist.getIdCompteCred() + " blalalalalaal");

        } catch (Exception e) {
            e.printStackTrace();
            ejbContext.setRollbackOnly();
            throw e;
        }

        // System.out.println();
    }

    // @Override
    // public Depot update(Depot depot) throws Exception {
    // if (depot == null) {
    // throw new Exception("Le dépôt ne doit pas être null");
    // }
    // return repository.update(depot);
    // }

    // @Override
    // public void delete(Depot depot) throws Exception {
    // if (depot == null) {
    // throw new Exception("Le dépôt ne doit pas être null");
    // }
    // repository.delete(depot);
    // }

    @Override
    public List<DepotDto> findByCompte(Integer idCompte) throws Exception {
        if (idCompte == null) {
            throw new Exception("L'id du compte ne doit pas être null");
        }
        // return new ArrayList<>();
        return repository.findByCompte(idCompte).stream().map(DepotMapper::toDto).collect(Collectors.toList());
    }
}
