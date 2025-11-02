package com.example.repositories;

import com.example.models.Role;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RoleRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    // Trouver un rôle par ID
    public Role findById(Integer id) {
        return em.find(Role.class, id);
    }

    // Récupérer la liste de tous les rôles
    public List<Role> findAll() {
        TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r", Role.class);
        return query.getResultList();
    }

    // Rechercher un rôle par libellé
    public Role findByLibelle(String libelle) {
        TypedQuery<Role> query = em.createQuery(
                "SELECT r FROM Role r WHERE r.libelle = :libelle", Role.class);
        query.setParameter("libelle", libelle);
        List<Role> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    // Sauvegarder (create/update)
    public void save(Role role) {
        if (role.getIdRole() == null) {
            em.persist(role);
        } else {
            em.merge(role);
        }
    }

    // Mise à jour
    public Role update(Role role) {
        return em.merge(role);
    }

    // Suppression
    public void delete(Role role) {
        em.remove(em.contains(role) ? role : em.merge(role));
    }
}
