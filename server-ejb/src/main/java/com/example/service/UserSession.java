package com.example.service;


import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

import com.example.models.ClientCourant;
import com.example.remotes.ClientCourantStatefulServiceRemote;

@Named
@SessionScoped
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ClientCourantStatefulServiceRemote sessionEJB;

    private ClientCourant client; 

    public ClientCourant getClient() {
        return client;
    }

    public void setClient(ClientCourant client) {
        this.client = client;
        sessionEJB.setClient(client);
    }

    public ClientCourantStatefulServiceRemote getSessionEJB() {
        return sessionEJB;
    }

}
