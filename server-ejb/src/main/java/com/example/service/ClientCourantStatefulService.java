package com.example.service;

import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.ClientCourantStatefulServiceRemote;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateful
public class ClientCourantStatefulService implements ClientCourantStatefulServiceRemote {

    @EJB
    private ClientCourantServiceRemote clientCourantServiceRemote;

    private String instanceId = UUID.randomUUID().toString();
    private ClientCourant client;
    private List<CompteCourant> comptes = new ArrayList<>();

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public void setClient(ClientCourant client) {
        this.client = client;
        if (client.getComptes() != null) {
            this.comptes = new ArrayList<>(client.getComptes());
        } else {
            this.comptes = new ArrayList<>();
        }
    }

    @Override
    public ClientCourant getClient() throws Exception {

        // try {
        // // if (this.client == null) {
        // // throw new Exception("Aucune session pour utilisateur");
        // // }
        // ClientCourant clientCourant =
        // clientCourantServiceRemote.getClientById(client.getIdClient()) ;
        // } catch (Exception e) {
        // throw e;
        // }

        return client;
    }

    @Override
    public void ajouterCompte(CompteCourant compte) {
        comptes.add(compte);
        compte.setClient(client);
    }

    @Override
    public List<CompteCourant> getComptes() {
        return new ArrayList<>(comptes);
    }
}
