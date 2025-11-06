package com.example.repositories;

import com.example.models.Etat;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class EtatRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    // CRUD basique

    public Etat findById(Integer id) {
        return em.find(Etat.class, id);
    }

    public List<Etat> findAll() {
        TypedQuery<Etat> query = em.createQuery(
                "SELECT e FROM Etat e", Etat.class);
        return query.getResultList();
    }

    public void create(Etat etat) {
        em.persist(etat);
    }

    public Etat update(Etat etat) {
        return em.merge(etat);
    }

    public void delete(Etat etat) {
        if (!em.contains(etat)) {
            etat = em.merge(etat);
        }
        em.remove(etat);
    }

    // Requêtes spécifiques : récupération des états par libelle
    public Etat findByLibelle(String libelle) {
        TypedQuery<Etat> query = em.createQuery(
                "SELECT e FROM Etat e WHERE e.libelle = :lib", Etat.class);
        query.setParameter("lib", libelle);
        return query.getResultStream().findFirst().orElse(null);
    }
}
