package com.example.ejb;

import com.example.api.CompteCourantApi;

import jakarta.ejb.Stateless;

@Stateless
public class CompteCourantEjb implements CompteCourantApi {

    @Override
    public double getSolde(Integer id_user) {
        return  190.2;
    }
}
