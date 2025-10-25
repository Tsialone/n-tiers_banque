package com.example.remotes;

import com.example.models.CompteCourant;

import java.time.LocalDate;
import java.util.List;

import jakarta.ejb.Local;
import jakarta.ejb.Remote;

@Remote
public interface CompteCourantServiceRemote {
    List<CompteCourant> getComptesByClientAndDate(Integer idClient , LocalDate date);
    CompteCourant getCompteById(Integer idCompte);
    List<CompteCourant> getComptesByClient(Integer idClient);
    List<CompteCourant> getAllComptes();
    CompteCourant saveCompte(CompteCourant compte);
    void deleteCompte(CompteCourant compte);
}
