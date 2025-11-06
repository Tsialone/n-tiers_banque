package com.example.service;


import com.example.mappers.TypeCompteMapper;
import com.example.remotes.TypeCompteServiceRemote;
import com.example.repositories.TypeCompteRepository;
import com.example.server_dtos.TypeCompteDto;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class TypeCompteService   implements TypeCompteServiceRemote {

    @EJB
    private TypeCompteRepository typeCompteRepository;

    @Override
    public TypeCompteDto findById(Integer idTypeCompte) {
        return  TypeCompteMapper.toDto(typeCompteRepository.findById(idTypeCompte), false) ;
    }
}