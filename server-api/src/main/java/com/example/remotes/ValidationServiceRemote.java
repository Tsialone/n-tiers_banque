package com.example.remotes;

import com.example.server_dtos.ValidationDto;
import jakarta.ejb.Remote;

@Remote
public interface ValidationServiceRemote {

 
    ValidationDto saveValidation(Integer idTransactionCourant, String etat) throws Exception;
}
