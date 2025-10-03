package com.example.service;

import com.example.models.ClientCourant;
import com.example.repositories.ClientCourantRepository;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.time.LocalDate;
import java.util.List;

import com.example.models.CompteCourant;

@Stateless
public class ClientCourantService {

    @EJB
    private ClientCourantRepository repository;

    @EJB
    private CompteCourantService compteCourantService;

    // getSoldeByIdClientAndIdCompte
    public double getSoldeByIdClientAndIdCompte(Integer idCompte, Integer idClient,LocalDate dateTransaction) throws Exception {
        try {
            CompteCourant compteCourant  = compteCourantService.getCompteById(idCompte);
            return repository.findSoldeByIdClientAndIdCompte(idCompte, idClient, dateTransaction) + compteCourant.getCapital();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Récupérer un client par son ID
    public ClientCourant getClientById(Integer idClient) {
        ClientCourant client = repository.findById(idClient);
        if (client == null) {
            throw new IllegalArgumentException("Client non trouvé : " + idClient);
        }
        return client;
    }

    // Lister tous les clients
    public List<ClientCourant> getAllClients() {
        return repository.findAll();
    }

    // Chercher par nom
    public List<ClientCourant> getClientsByNom(String nom) {
        return repository.findByNom(nom);
    }

    // Chercher par date de naissance (avant une date donnée)
    public List<ClientCourant> getClientsBornBefore(LocalDate date) {
        return repository.findByDateNaissanceBefore(date);
    }

    // Ajouter ou mettre à jour un client
    public ClientCourant saveClient(ClientCourant client) {
        repository.save(client);
        return client;
    }

    // Supprimer un client
    public void deleteClient(ClientCourant client) {
        repository.delete(client);
    }
}
