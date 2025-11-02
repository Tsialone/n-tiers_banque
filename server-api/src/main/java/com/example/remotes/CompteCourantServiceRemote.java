package com.example.remotes;

import java.time.LocalDate;
import java.util.List;

import com.example.server_dtos.CompteCourantDto;

import jakarta.ejb.Local;
import jakarta.ejb.Remote;

@Remote
public interface CompteCourantServiceRemote {
    List<CompteCourantDto> getComptesByClientAndDate(Integer idClient , LocalDate date);
    CompteCourantDto getCompteById(Integer idCompte);
    List<CompteCourantDto> getComptesByClient(Integer idClient);
    List<CompteCourantDto> getAllComptes();
    CompteCourantDto saveCompte(CompteCourantDto compte );
    void deleteCompte(CompteCourantDto compte);
}
