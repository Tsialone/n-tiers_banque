package com.example.repositories;

import com.example.models.TransactionCourant;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class TransactionCourantRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    // CRUD basique

    public List<TransactionCourant> findByDevise(String devise) {
        TypedQuery<TransactionCourant> query = em.createQuery(
                "SELECT t FROM TransactionCourant t WHERE LOWER(t.devise) = LOWER(:devise)",
                TransactionCourant.class);
        query.setParameter("devise", devise);
        return query.getResultList();
    }

    public TransactionCourant findById(Integer id) {
        return em.find(TransactionCourant.class, id);
    }

    public List<TransactionCourant> findAll() {
        TypedQuery<TransactionCourant> query = em.createQuery(
                "SELECT t FROM TransactionCourant t", TransactionCourant.class);
        return query.getResultList();
    }

    public void create(TransactionCourant transaction) {
        em.persist(transaction);
    }

    public TransactionCourant update(TransactionCourant transaction) {
        return em.merge(transaction);
    }

    public void delete(TransactionCourant transaction) {
        if (!em.contains(transaction)) {
            transaction = em.merge(transaction);
        }
        em.remove(transaction);
    }

    // Méthodes supplémentaires

    // public TransactionCourant findBySource(String source) {
    // try {
    // TypedQuery<TransactionCourant> query = em.createQuery(
    // "SELECT t FROM TransactionCourant t WHERE t.source = :source",
    // TransactionCourant.class);
    // query.setParameter("source", source);
    // return query.getSingleResult();
    // } catch (NoResultException e) {
    // return null; // ou tu peux lever une exception personnalisée
    // }
    // }

    public List<TransactionCourant> findBySource(String source) {
        TypedQuery<TransactionCourant> query = em.createQuery(
                "SELECT t FROM TransactionCourant t WHERE t.source = :source", TransactionCourant.class);
        query.setParameter("source", source);
        return query.getResultList();
    }

    public List<TransactionCourant> findBySens(String sens) {
        TypedQuery<TransactionCourant> query = em.createQuery(
                "SELECT t FROM TransactionCourant t WHERE t.sens = :sens", TransactionCourant.class);
        query.setParameter("sens", sens);
        return query.getResultList();
    }
}
