package com.example.repositories;

import com.example.models.HistoriqueVirement;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class HistoriqueVirementRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public void create(HistoriqueVirement historique) {
        em.persist(historique);
    }

    public HistoriqueVirement update(HistoriqueVirement historique) {
        return em.merge(historique);
    }

    public List<HistoriqueVirement> findByVirementId(Integer idVirement) {
        TypedQuery<HistoriqueVirement> query = em.createQuery(
                "SELECT h FROM HistoriqueVirement h WHERE h.virement.idVirement = :vId ORDER BY h.dateAction DESC",
                HistoriqueVirement.class
        );
        query.setParameter("vId", idVirement);
        return query.getResultList();
    }

    public List<HistoriqueVirement> findAll() {
        TypedQuery<HistoriqueVirement> query = em.createQuery(
                "SELECT h FROM HistoriqueVirement h ORDER BY h.dateAction DESC", HistoriqueVirement.class
        );
        return query.getResultList();
    }
}
