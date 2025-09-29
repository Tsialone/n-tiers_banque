package com.example.repositories;

import com.example.models.TransactionCourant;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class TransactionCourantRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public TransactionCourant findById(Integer id) {
        return em.find(TransactionCourant.class, id);
    }

    public List<TransactionCourant> getAll() {
        TypedQuery<TransactionCourant> query = em.createQuery(
                "SELECT t FROM TransactionCourant t", TransactionCourant.class);
        return query.getResultList();
    }

    public List<TransactionCourant> findByCompteId(Integer idCompte) {
        TypedQuery<TransactionCourant> query = em.createQuery(
                "SELECT t FROM TransactionCourant t WHERE t.compte.idCompte = :idCompte",
                TransactionCourant.class);
        query.setParameter("idCompte", idCompte);
        return query.getResultList();
    }

    public void save(TransactionCourant transaction) {
        if (transaction.getIdTransaction() == null) {
            em.persist(transaction);
        } else {
            em.merge(transaction);
        }
    }

    public void delete(TransactionCourant transaction) {
        em.remove(em.contains(transaction) ? transaction : em.merge(transaction));
    }
}
