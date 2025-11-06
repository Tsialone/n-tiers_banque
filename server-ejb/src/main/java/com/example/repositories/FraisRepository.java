package com.example.repositories;

import com.example.models.Frais;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class FraisRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public Frais findById(Integer id) {
        return em.find(Frais.class, id);
    }

    public List<Frais> findAll() {
        TypedQuery<Frais> query = em.createQuery("SELECT f FROM Frais f", Frais.class);
        return query.getResultList();
    }

    public void create(Frais frais) {
        em.persist(frais);
    }

    public Frais update(Frais frais) {
        return em.merge(frais);
    }

    public void delete(Frais frais) {
        if (!em.contains(frais)) {
            frais = em.merge(frais);
        }
        em.remove(frais);
    }

    public List<Frais> findByTypeCompteId(Integer typeCompteId) {
        TypedQuery<Frais> query = em.createQuery(
                "SELECT f FROM Frais f " +
                        "WHERE f.typeCompte.idTypeCompte = :id " +
                        "ORDER BY f.montantInf ASC, f.montantSup ASC",
                Frais.class);
        query.setParameter("id", typeCompteId);
        return query.getResultList();
    }

    public Frais findCurrentFrais(Integer typeCompteId) {
        TypedQuery<Frais> query = em.createQuery(
                "SELECT f FROM Frais f WHERE f.typeCompte.idTypeCompte = :id ORDER BY f.dateCreation DESC",
                Frais.class);
        query.setParameter("id", typeCompteId);
        query.setMaxResults(1);
        List<Frais> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}
