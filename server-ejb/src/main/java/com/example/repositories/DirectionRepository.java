package com.example.repositories;

import com.example.models.Direction;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class DirectionRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    // Récupérer par ID
    public Direction findById(Integer id) {
        return em.find(Direction.class, id);
    }

    // Récupérer toutes les directions
    public List<Direction> findAll() {
        TypedQuery<Direction> query = em.createQuery(
                "SELECT d FROM Direction d", Direction.class);
        return query.getResultList();
    }

    // Sauvegarder une nouvelle direction
    public void save(Direction direction) {
        if (direction.getIdDirection() == null) {
            em.persist(direction);
        } else {
            em.merge(direction);
        }
    }

    // Mettre à jour une direction existante
    public Direction update(Direction direction) {
        return em.merge(direction);
    }

    // Supprimer une direction
    public void delete(Direction direction) {
        em.remove(em.contains(direction) ? direction : em.merge(direction));
    }
}
