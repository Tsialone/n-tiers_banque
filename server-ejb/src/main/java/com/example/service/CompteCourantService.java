package com.example.service;

import com.example.models.CompteCourant;
import com.example.remotes.CompteCourantServiceRemote;
import com.example.repositories.CompteCourantRepository;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class CompteCourantService implements CompteCourantServiceRemote  {

    @EJB
    private CompteCourantRepository repository;


    // recuperer le solde par id_compte et utilisateur
    
    // Récupérer un compte par ID
    public CompteCourant getCompteById(Integer idCompte) {
        CompteCourant compte = repository.findById(idCompte);
        if (compte == null) {
            throw new IllegalArgumentException("Compte non trouvé : " + idCompte);
        }
        return compte;
    }

    // Récupérer tous les comptes d'un client
    public List<CompteCourant> getComptesByClient(Integer idClient) {
        return repository.findByClientId(idClient);
    }

    // Récupérer tous les comptes
    public List<CompteCourant> getAllComptes() {
        return repository.findAll(); // Assure-toi que repository a bien la méthode getAll()
    }

    // Ajouter ou mettre à jour un compte
    public CompteCourant saveCompte(CompteCourant compte) {
        repository.save(compte);
        return compte;
    }

    // Supprimer un compte
    public void deleteCompte(CompteCourant compte) {
        repository.delete(compte);
    }
}
