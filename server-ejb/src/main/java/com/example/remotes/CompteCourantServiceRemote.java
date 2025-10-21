package com.example.remotes;

import com.example.models.CompteCourant;
import java.util.List;
import jakarta.ejb.Remote;

@Remote
public interface CompteCourantServiceRemote {
    CompteCourant getCompteById(Integer idCompte);
    List<CompteCourant> getComptesByClient(Integer idClient);
    List<CompteCourant> getAllComptes();
    CompteCourant saveCompte(CompteCourant compte);
    void deleteCompte(CompteCourant compte);
}
