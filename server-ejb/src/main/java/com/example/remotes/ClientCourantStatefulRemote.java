package com.example.remotes;

import com.example.models.ClientCourant;
import com.example.models.CompteCourant;

import jakarta.ejb.Remote;
import java.util.List;

@Remote
public interface ClientCourantStatefulRemote {

    // Sélectionner le client courant pour la session
    void setClient(ClientCourant client);

    // Récupérer le client courant
    ClientCourant getClient();

    // Ajouter un compte au client courant
    void ajouterCompte(CompteCourant compte);

    // Lister les comptes du client courant
    List<CompteCourant> getComptes();
}
