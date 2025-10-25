package com.example.repositories;

import com.example.models.CompteCourant;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class CompteCourantRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public List<CompteCourant> findByClientIdAndDate(Integer idClient, LocalDate date) {

        String jpql = "SELECT c FROM CompteCourant c WHERE c.client.idClient = :idClient";

        if (date != null) {
            jpql += " AND c.dateOuverture <= :date";
        }

        TypedQuery<CompteCourant> query = em.createQuery(jpql, CompteCourant.class);
        query.setParameter("idClient", idClient);

        if (date != null) {
            query.setParameter("date", date);
        }

        return query.getResultList();
    }

    public List<CompteCourant> findAll() {
        return em.createQuery("SELECT c FROM CompteCourant c", CompteCourant.class)
                .getResultList();
    }

    public CompteCourant findById(Integer id) {
        return em.find(CompteCourant.class, id);
    }

    public List<CompteCourant> findByClientId(Integer idClient) {
        TypedQuery<CompteCourant> query = em.createQuery(
                "SELECT c FROM CompteCourant c WHERE c.client.idClient = :idClient", CompteCourant.class);
        query.setParameter("idClient", idClient);
        return query.getResultList();
    }

    public void save(CompteCourant compte) {
        if (compte.getIdCompte() == null) {
            em.persist(compte);
        } else {
            em.merge(compte);
        }
    }

    public void delete(CompteCourant compte) {
        em.remove(em.contains(compte) ? compte : em.merge(compte));
    }
}
