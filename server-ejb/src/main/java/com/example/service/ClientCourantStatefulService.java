package com.example.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

import com.example.models.ClientCourant;
import com.example.models.ClientRole;
import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.repositories.ClientCourantRepository;

@Stateful
@Remote(ClientCourantStatefulServiceRemote.class)
public class ClientCourantStatefulService implements ClientCourantStatefulServiceRemote, Serializable {

    @EJB
    private ClientCourantRepository repository;

    private ClientCourant client;

    public ClientCourant getClient() {
        return client;
    }

    // @Remove
    // public void logout() {
    //     this.client = null;
    // }

    public ClientCourant login(String email, String mdp) throws Exception {
        try {
            ClientCourant clientCourant = repository.findByEmail(email);

            if (clientCourant == null)
                throw new Exception("Email non trouver");
            if (!clientCourant.getMdp().equals(mdp))
                throw new Exception("Mot de passe incorrect");
            else {
                clientCourant.getComptes().size();
                clientCourant.getClientRoles().size();
                for (ClientRole clientRole : clientCourant.getClientRoles()) {
                    clientRole.getRole().getActionRoles().size();
                }
            }
            this.setClient(clientCourant);
            return clientCourant;
        } catch (Exception e) {
            throw e;
        }
    }

    public void setClient(ClientCourant client) {
        this.client = client;
    }

    public void clear() {
        client = null;
    }

}
