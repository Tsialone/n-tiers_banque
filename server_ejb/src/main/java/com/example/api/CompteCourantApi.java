package com.example.api;

import jakarta.ejb.Remote;

@Remote
public interface CompteCourantApi {
    double getSolde(Integer id_user);
}


