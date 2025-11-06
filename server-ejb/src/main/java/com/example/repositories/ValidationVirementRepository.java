package com.example.repositories;

import com.example.models.ValidationVirement;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ValidationVirementRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    // CRUD basique

    public ValidationVirement findById(Integer id) {
        return em.find(ValidationVirement.class, id);
    }

    public List<ValidationVirement> findAll() {
        TypedQuery<ValidationVirement> query = em.createQuery(
                "SELECT v FROM ValidationVirement v", ValidationVirement.class);
        return query.getResultList();
    }

    public void create(ValidationVirement validation) {
        em.persist(validation);
    }

    public ValidationVirement update(ValidationVirement validation) {
        return em.merge(validation);
    }

    public void delete(ValidationVirement validation) {
        if (!em.contains(validation)) {
            validation = em.merge(validation);
        }
        em.remove(validation);
    }

    // Requêtes spécifiques

    public List<ValidationVirement> findByVirementId(Integer virementId) {
        TypedQuery<ValidationVirement> query = em.createQuery(
                "SELECT v FROM ValidationVirement v WHERE v.virement.idVirement = :vId", ValidationVirement.class);
        query.setParameter("vId", virementId);
        return query.getResultList();
    }

    public List<ValidationVirement> findByEtatId(Integer etatId) {
        TypedQuery<ValidationVirement> query = em.createQuery(
                "SELECT v FROM ValidationVirement v WHERE v.etat.idEtat = :eId", ValidationVirement.class);
        query.setParameter("eId", etatId);
        return query.getResultList();
    }
}
