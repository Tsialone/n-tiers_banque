package com.example.repositories;

import com.example.models.ValidationTransaction;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ValidationTransactionRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public void save(ValidationTransaction validation) {
        em.persist(validation);
    }

    public ValidationTransaction update(ValidationTransaction validation) {
        return em.merge(validation);
    }

    public void delete(ValidationTransaction validation) {
        if (!em.contains(validation)) {
            validation = em.merge(validation);
        }
        em.remove(validation);
    }

    public ValidationTransaction findById(Integer id) {
        return em.find(ValidationTransaction.class, id);
    }

    public List<ValidationTransaction> findAll() {
        return em.createQuery("SELECT v FROM ValidationTransaction v", ValidationTransaction.class)
                 .getResultList();
    }

    public List<ValidationTransaction> findByTransactionId(Integer idTransaction) {
        TypedQuery<ValidationTransaction> query = em.createQuery(
            "SELECT v FROM ValidationTransaction v WHERE v.transaction.idTransaction = :idTransaction ORDER BY v.dateValidation DESC",
            ValidationTransaction.class);
        query.setParameter("idTransaction", idTransaction);
        return query.getResultList();
    }

    public ValidationTransaction findLastValidation(Integer idTransaction) {
        try {
            TypedQuery<ValidationTransaction> query = em.createQuery(
                "SELECT v FROM ValidationTransaction v WHERE v.transaction.idTransaction = :idTransaction ORDER BY v.dateValidation DESC",
                ValidationTransaction.class);
            query.setParameter("idTransaction", idTransaction);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
