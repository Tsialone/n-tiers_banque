package com.example.service;

import com.example.mappers.ClientCourantMapper;
import com.example.mappers.CompteCourantMapper;
import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.models.TypeCompte;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.repositories.ClientCourantRepository;
import com.example.repositories.CompteCourantRepository;
import com.example.repositories.TypeCompteRepository;
import com.example.server_dtos.CompteCourantDto;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class CompteCourantService implements CompteCourantServiceRemote {

    @EJB
    private CompteCourantRepository repository;

    @EJB
    private ClientCourantRepository clientCourantRepository;

    @EJB
    private TypeCompteRepository typeCompteRepository;

    @Override
    public List<CompteCourantDto> getComptesByClientAndDate(Integer idClient, LocalDate date) {
        return repository.findByClientIdAndDate(idClient, date)
                .stream()
                .map(CompteCourantMapper::toDto)
                .collect(Collectors.toList());
    }

    // recuperer le solde par id_compte et utilisateur

    // Récupérer un compte par ID
    @Override
    public CompteCourantDto getCompteById(Integer idCompte) {
        CompteCourant compte = repository.findById(idCompte);
        if (compte == null) {
            throw new IllegalArgumentException("Compte non trouvé : " + idCompte);
        }
        return CompteCourantMapper.toDto(compte);
    }

    // Récupérer tous les comptes d'un client
    @Override
    public List<CompteCourantDto> getComptesByClient(Integer idClient) {
        return repository.findByClientId(idClient)
                .stream()
                .map(CompteCourantMapper::toDto)
                .collect(Collectors.toList());

    }

    // Récupérer tous les comptes
    @Override
    public List<CompteCourantDto> getAllComptes() {
        return repository.findAll()
                .stream()
                .map(CompteCourantMapper::toDto)
                .collect(Collectors.toList());
    }

    // Ajouter ou mettre à jour un compte
    @Override
    public CompteCourantDto saveCompte(CompteCourantDto compte) {
        ClientCourant clientCourant = clientCourantRepository.findById(compte.getIdClient());
         TypeCompte typeCompte =  typeCompteRepository.findById(compte.getTypeCompte().getIdTypeCompte());
        repository.save(CompteCourantMapper.toEntity(compte, clientCourant , typeCompte));
        return compte;
    }

    // Supprimer un compte
    @Override
    public void deleteCompte(CompteCourantDto compte) {
        CompteCourant compteCourant = repository.findById(compte.getIdCompte());
        repository.delete(compteCourant);
    }
}
