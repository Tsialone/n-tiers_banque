package com.example.repositories;

import com.example.models.Retrait;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RetraitRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public Retrait findById(Integer id) {
        return em.find(Retrait.class, id);
    }

    public Retrait findBySource(String source) {
        TypedQuery<Retrait> query = em.createQuery(
                "SELECT r FROM Retrait r WHERE r.idObject = :source", Retrait.class);
        query.setParameter("source", source);
        List<Retrait> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Retrait> findAll() {
        TypedQuery<Retrait> query = em.createQuery("SELECT r FROM Retrait r", Retrait.class);
        return query.getResultList();
    }

    public void create(Retrait retrait) {
        if (retrait.getIdObject() == null || retrait.getIdObject().isEmpty()) {
            retrait.setIdObject(generateIdObject());
        }
        em.persist(retrait);
    }

    public Retrait update(Retrait retrait) {
        return em.merge(retrait);
    }

    public void delete(Retrait retrait) {
        if (!em.contains(retrait)) {
            retrait = em.merge(retrait);
        }
        em.remove(retrait);
    }

    public List<Retrait> findByCompte(Integer idCompte) {
        TypedQuery<Retrait> query = em.createQuery(
                "SELECT r FROM Retrait r WHERE r.compteDebit.idCompte = :idCompte",
                Retrait.class);
        query.setParameter("idCompte", idCompte);
        return query.getResultList();
    }

    public String generateIdObject() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(r) FROM Retrait r", Long.class);
        Long count = query.getSingleResult();
        return "rtr_" + (count + 1);
    }
}
