package com.example.repositories;

import com.example.models.HistoriqueRetrait;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class HistoriqueRetraitRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public HistoriqueRetrait findById(Integer id) {
        return em.find(HistoriqueRetrait.class, id);
    }

    public List<HistoriqueRetrait> findAll() {
        TypedQuery<HistoriqueRetrait> query = em.createQuery(
            "SELECT h FROM HistoriqueRetrait h ORDER BY h.dateHistorique DESC",
            HistoriqueRetrait.class
        );
        return query.getResultList();
    }

    public void create(HistoriqueRetrait historique) {
        em.persist(historique);
    }

    public HistoriqueRetrait update(HistoriqueRetrait historique) {
        return em.merge(historique);
    }

    public void delete(HistoriqueRetrait historique) {
        if (!em.contains(historique)) {
            historique = em.merge(historique);
        }
        em.remove(historique);
    }

    public List<HistoriqueRetrait> findByRetrait(Integer idRetrait) {
        TypedQuery<HistoriqueRetrait> query = em.createQuery(
            "SELECT h FROM HistoriqueRetrait h WHERE h.retrait.idRetrait = :idRetrait ORDER BY h.dateHistorique DESC",
            HistoriqueRetrait.class
        );
        query.setParameter("idRetrait", idRetrait);
        return query.getResultList();
    }
}
