package com.example.repositories;

import com.example.models.TypeCompte;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class TypeCompteRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    // CRUD basique
    public TypeCompte findById(Integer id) {
        return em.find(TypeCompte.class, id);
    }

    public List<TypeCompte> findAll() {
        TypedQuery<TypeCompte> query = em.createQuery(
                "SELECT t FROM TypeCompte t", TypeCompte.class);
        return query.getResultList();
    }

    public void create(TypeCompte typeCompte) {
        em.persist(typeCompte);
    }

    public TypeCompte update(TypeCompte typeCompte) {
        return em.merge(typeCompte);
    }

    public void delete(TypeCompte typeCompte) {
        if (!em.contains(typeCompte)) {
            typeCompte = em.merge(typeCompte);
        }
        em.remove(typeCompte);
    }
}
