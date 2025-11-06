package com.example.repositories;

import com.example.models.ValidationRetrait;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ValidationRetraitRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public ValidationRetrait findById(Integer id) {
        return em.find(ValidationRetrait.class, id);
    }

    public List<ValidationRetrait> findAll() {
        TypedQuery<ValidationRetrait> query = em.createQuery(
                "SELECT v FROM ValidationRetrait v", ValidationRetrait.class);
        return query.getResultList();
    }

    public void create(ValidationRetrait validationRetrait) {
        em.persist(validationRetrait);
    }

    public ValidationRetrait update(ValidationRetrait validationRetrait) {
        return em.merge(validationRetrait);
    }

    public void delete(ValidationRetrait validationRetrait) {
        if (!em.contains(validationRetrait)) {
            validationRetrait = em.merge(validationRetrait);
        }
        em.remove(validationRetrait);
    }

    public List<ValidationRetrait> findByRetrait(Integer idRetrait) {
        TypedQuery<ValidationRetrait> query = em.createQuery(
                "SELECT v FROM ValidationRetrait v WHERE v.retrait.idRetrait = :idRetrait",
                ValidationRetrait.class);
        query.setParameter("idRetrait", idRetrait);
        return query.getResultList();
    }


}
