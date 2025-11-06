package com.example.repositories;

import com.example.models.Virement;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class VirementRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    // CRUD basique

    public Virement findById(Integer id) {
        return em.find(Virement.class, id);
    }

    public List<Virement> findByDevise(String devise) {
        TypedQuery<Virement> query = em.createQuery(
                "SELECT v FROM Virement v WHERE LOWER(v.devise) = LOWER(:devise)",
                Virement.class);
        query.setParameter("devise", devise);
        return query.getResultList();
    }

    public List<Virement> findAll() {
        TypedQuery<Virement> query = em.createQuery(
                "SELECT v FROM Virement v", Virement.class);
        return query.getResultList();
    }

    public void create(Virement virement) {
        if (virement.getIdObject() == null || virement.getIdObject().isEmpty()) {
            virement.setIdObject(generateIdObject());
        }
        em.persist(virement);
    }

    public Virement update(Virement virement) {
        return em.merge(virement);
    }

    public void delete(Virement virement) {
        if (!em.contains(virement)) {
            virement = em.merge(virement);
        }
        em.remove(virement);
    }

    // Requêtes spécifiques
    public List<Virement> findByCompteDebitOrCredit(Integer idCompte) {
        TypedQuery<Virement> query = em.createQuery(
                "SELECT v FROM Virement v WHERE v.compteDebit.idCompte = :id OR v.compteCredit.idCompte = :id",
                Virement.class);
        query.setParameter("id", idCompte);
        return query.getResultList();
    }

    public List<Virement> findByCompteDebit(Integer idCompte) {
        TypedQuery<Virement> query = em.createQuery(
                "SELECT v FROM Virement v WHERE v.compteDebit.idCompte = :id", Virement.class);
        query.setParameter("id", idCompte);
        return query.getResultList();
    }

    public List<Virement> findByCompteCredit(Integer idCompte) {
        TypedQuery<Virement> query = em.createQuery(
                "SELECT v FROM Virement v WHERE v.compteCredit.idCompte = :id", Virement.class);
        query.setParameter("id", idCompte);
        return query.getResultList();
    }

    // Génération automatique de l'idObject (ex: vrmt_1, vrmt_2)
    public String generateIdObject() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(v) FROM Virement v", Long.class);
        Long count = query.getSingleResult();
        return "vrmt_" + (count + 1);
    }

    public Virement findBySource(String source) {
        if (source == null)
            return null;
        TypedQuery<Virement> query = em.createQuery(
                "SELECT v FROM Virement v WHERE v.idObject = :source", Virement.class);
        query.setParameter("source", source);
        List<Virement> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
