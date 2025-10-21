package com.example.service;

import com.example.models.ClientCourant;
import com.example.repositories.ClientCourantRepository;

import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;

import java.time.LocalDate;
import java.util.List;

import com.example.models.CompteCourant;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.ClientCourantStatefulRemote;
import com.example.remotes.CompteCourantServiceRemote;

@Stateless
@Remote(ClientCourantServiceRemote.class)
public class ClientCourantService implements ClientCourantServiceRemote {

    @EJB
    private ClientCourantRepository repository;

    @EJB
    private CompteCourantServiceRemote compteCourantService;

    @EJB
    private ClientCourantStatefulRemote clientCourantStatefulService;


     public ClientCourant getUtilisateur() {

        try {
          return   clientCourantStatefulService.getClient();
        } catch (Exception e) {
            throw e;
        }
    }

    public ClientCourant login(Integer idClient) {

        try {
            ClientCourant clientCourant = getClientById(idClient);
            clientCourant.getComptes().size();
            clientCourantStatefulService.setClient(clientCourant);
            return clientCourant;
        } catch (Exception e) {
            throw e;
        }
    }

    // getSoldeByIdClientAndIdCompte
    public double getSoldeByIdClientAndIdCompte(Integer idCompte, Integer idClient, LocalDate dateTransaction)
            throws Exception {
        try {

            CompteCourant compteCourant = compteCourantService.getCompteById(idCompte);
            return repository.findSoldeByIdClientAndIdCompte(idCompte, idClient, dateTransaction)
                    + compteCourant.getCapital();
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
