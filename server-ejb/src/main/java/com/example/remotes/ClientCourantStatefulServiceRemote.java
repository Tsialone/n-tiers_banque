package com.example.remotes;

import com.example.models.ClientCourant;
import com.example.models.CompteCourant;

import jakarta.ejb.Remote;
import java.util.List;

@Remote
public interface ClientCourantStatefulServiceRemote {

    ClientCourant login (String email  , String mdp) throws Exception;
    // Sélectionner le client courant pour la session
    void setClient(ClientCourant client);

    // Récupérer le client courant
    ClientCourant getClient() throws Exception;

}
