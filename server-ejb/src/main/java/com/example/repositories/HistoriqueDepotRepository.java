package com.example.repositories;

import com.example.models.HistoriqueDepot;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class HistoriqueDepotRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public HistoriqueDepot findById(Integer id) {
        return em.find(HistoriqueDepot.class, id);
    }

    public List<HistoriqueDepot> findAll() {
        TypedQuery<HistoriqueDepot> query = em.createQuery(
            "SELECT h FROM HistoriqueDepot h ORDER BY h.dateHistorique DESC",
            HistoriqueDepot.class
        );
        return query.getResultList();
    }

    public void create(HistoriqueDepot historique) {
        em.persist(historique);
    }

    public HistoriqueDepot update(HistoriqueDepot historique) {
        return em.merge(historique);
    }

    public void delete(HistoriqueDepot historique) {
        if (!em.contains(historique)) {
            historique = em.merge(historique);
        }
        em.remove(historique);
    }

    public List<HistoriqueDepot> findByDepot(Integer idDepot) {
        TypedQuery<HistoriqueDepot> query = em.createQuery(
            "SELECT h FROM HistoriqueDepot h WHERE h.depot.idDepot = :idDepot ORDER BY h.dateHistorique DESC",
            HistoriqueDepot.class
        );
        query.setParameter("idDepot", idDepot);
        return query.getResultList();
    }
}
