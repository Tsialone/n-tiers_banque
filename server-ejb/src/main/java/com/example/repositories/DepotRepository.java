package com.example.repositories;

import com.example.models.Depot;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class DepotRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public Depot findById(Integer id) {
        return em.find(Depot.class, id);
    }

    public Depot findBySource(String source) {
        TypedQuery<Depot> query = em.createQuery(
                "SELECT d FROM Depot d WHERE d.idObject = :source", Depot.class);
        query.setParameter("source", source);
        List<Depot> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Depot> findAll() {
        TypedQuery<Depot> query = em.createQuery("SELECT d FROM Depot d", Depot.class);
        return query.getResultList();
    }

    public void create(Depot depot) {
        if (depot.getIdObject() == null || depot.getIdObject().isEmpty()) {
            depot.setIdObject(generateIdObject());
        }
        em.persist(depot);
        // em.flush();
    }

    public Depot update(Depot depot) {
        return em.merge(depot);
    }

    public void delete(Depot depot) {
        if (!em.contains(depot)) {
            depot = em.merge(depot);
        }
        em.remove(depot);
    }

    public List<Depot> findByCompte(Integer idCompte) {
        TypedQuery<Depot> query = em.createQuery(
                "SELECT d FROM Depot d WHERE d.compteCredit.idCompte = :idCompte",
                Depot.class);
        query.setParameter("idCompte", idCompte);
        return query.getResultList();
    }

    public String generateIdObject() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(d) FROM Depot d", Long.class);
        Long count = query.getSingleResult();
        return "dpt_" + (count + 1);
    }
}
