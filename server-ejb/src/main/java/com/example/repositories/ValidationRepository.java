package com.example.repositories;

import com.example.models.TransactionCourant;
import com.example.models.Validation;
import com.example.server_dtos.ValidationDto;
import com.example.mappers.ValidationMapper;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ValidationRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public Validation findById(Integer id) {
        return em.find(Validation.class, id);
    }

    public List<Validation> findAll() {
        TypedQuery<Validation> query = em.createQuery(
                "SELECT v FROM Validation v", Validation.class);
        return query.getResultList();
    }

    public List<Validation> findByTransaction(TransactionCourant transaction) {
        TypedQuery<Validation> query = em.createQuery(
                "SELECT v FROM Validation v WHERE v.transaction = :transaction ORDER BY v.dateValidation DESC",
                Validation.class);
        query.setParameter("transaction", transaction);
        return query.getResultList();
    }

    public Validation findLastByTransaction(TransactionCourant transaction) {
        TypedQuery<Validation> query = em.createQuery(
                "SELECT v FROM Validation v WHERE v.transaction = :transaction ORDER BY v.dateValidation DESC",
                Validation.class);
        query.setParameter("transaction", transaction);
        query.setMaxResults(1);
        List<Validation> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    // Sauvegarder une validation
    public void save(Validation validation) {
        if (validation.getIdValidation() == null) {
            em.persist(validation);
        } else {
            em.merge(validation);
        }
    }

    public Validation update(Validation validation) {
        return em.merge(validation);
    }

    public void delete(Validation validation) {
        em.remove(em.contains(validation) ? validation : em.merge(validation));
    }

    public ValidationDto saveFromDto(ValidationDto dto, TransactionCourant transaction) {
        Validation validation = ValidationMapper.toEntity(dto, transaction);
        save(validation);
        return ValidationMapper.toDto(validation);
    }
}
