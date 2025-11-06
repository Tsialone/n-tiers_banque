package com.example.remotes;

import com.example.server_dtos.TypeCompteDto;
import com.example.server_dtos.ValidationDto;
import jakarta.ejb.Remote;

@Remote
public interface TypeCompteServiceRemote {

 
    TypeCompteDto findById(Integer idTypeCompte ) throws Exception;
}
