package com.example.repositories;

import com.example.models.ValidationDepot;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ValidationDepotRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public ValidationDepot findById(Integer id) {
        return em.find(ValidationDepot.class, id);
    }

    public void create(ValidationDepot validation) {
        em.persist(validation);
    }

    public ValidationDepot update(ValidationDepot validation) {
        return em.merge(validation);
    }

    public void delete(ValidationDepot validation) {
        if (!em.contains(validation)) {
            validation = em.merge(validation);
        }
        em.remove(validation);
    }

    public List<ValidationDepot> findAll() {
        TypedQuery<ValidationDepot> query = em.createQuery(
            "SELECT v FROM ValidationDepot v ORDER BY v.dateValidation DESC",
            ValidationDepot.class
        );
        return query.getResultList();
    }

    public List<ValidationDepot> findByDepot(Integer idDepot) {
        TypedQuery<ValidationDepot> query = em.createQuery(
            "SELECT v FROM ValidationDepot v WHERE v.depot.id = :idDepot ORDER BY v.dateValidation DESC",
            ValidationDepot.class
        );
        query.setParameter("idDepot", idDepot);
        return query.getResultList();
    }
}
