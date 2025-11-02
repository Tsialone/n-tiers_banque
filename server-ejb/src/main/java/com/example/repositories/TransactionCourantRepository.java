package com.example.repositories;

import com.example.mappers.TransactionCourantMapper;
import com.example.models.CompteCourant;
import com.example.models.TransactionCourant;
import com.example.server_dtos.TransactionCourantDto;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class TransactionCourantRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public TransactionCourant findById(Integer id) {
        return em.find(TransactionCourant.class, id);
    }

    public List<TransactionCourant> findVirementByClientIdAndDate(Integer idClient, LocalDate date) {
        TypedQuery<TransactionCourant> query = em.createQuery(
                "SELECT t FROM TransactionCourant t " +
                        "WHERE t.compte.client.idClient = :idClient " +
                        "AND t.dateTransaction = :date " + 
                        "AND t.idVirement IS NOT NULL",
                        
                TransactionCourant.class);
        query.setParameter("idClient", idClient);
        query.setParameter("date", date);
        return query.getResultList();
    }

    public List<TransactionCourant> getAll() {
        TypedQuery<TransactionCourant> query = em.createQuery(
                "SELECT t FROM TransactionCourant t", TransactionCourant.class);
        return query.getResultList();
    }

    public List<TransactionCourant> findByVirementId(Integer idVirement) {
        TypedQuery<TransactionCourant> query = em.createQuery(
                "SELECT t FROM TransactionCourant t WHERE t.idVirement = :idVirement",
                TransactionCourant.class);
        query.setParameter("idVirement", idVirement);
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

    public TransactionCourant update(TransactionCourant transaction) {
        return em.merge(transaction);
    }

    public TransactionCourantDto updateByDto(TransactionCourantDto transactionCourantDto, CompteCourant compteCourant) {
        em.merge(TransactionCourantMapper.toEntity(transactionCourantDto, compteCourant, null));
        return transactionCourantDto;
    }

    public void delete(TransactionCourant transaction) {
        em.remove(em.contains(transaction) ? transaction : em.merge(transaction));
    }
}
