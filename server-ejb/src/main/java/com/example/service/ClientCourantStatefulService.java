package com.example.service;

import com.example.models.ClientCourant;
import com.example.models.CompteCourant;
import com.example.remotes.ClientCourantStatefulRemote;

import jakarta.ejb.Stateful;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class ClientCourantStatefulService implements ClientCourantStatefulRemote {

    private ClientCourant client;
    private List<CompteCourant> comptes = new ArrayList<>();

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
    public ClientCourant getClient() {
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
