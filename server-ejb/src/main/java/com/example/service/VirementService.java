package com.example.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class VirementService {

    @PersistenceContext
    private EntityManager em;

    public Integer getNewId() {
        Integer virementId = ((Number) em
                .createNativeQuery("SELECT nextval('seq_virement')")
                .getSingleResult())
                .intValue();

        return virementId;
    }

}
