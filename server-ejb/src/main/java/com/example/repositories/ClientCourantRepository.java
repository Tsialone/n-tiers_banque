package com.example.repositories;

import com.example.models.ClientCourant;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class ClientCourantRepository {

    @PersistenceContext(unitName = "ejbPU")
    private EntityManager em;

    public Double findSoldeByIdClientAndIdCompte(
            Integer idCompte,
            Integer idClient,
            LocalDate dateTransaction) {
        String jpql;
        if (dateTransaction == null) {

            jpql = "SELECT SUM(CASE WHEN t.sens = 'credit' THEN t.montant ELSE -t.montant END) " +
                    "FROM TransactionCourant t " +
                    "WHERE t.compte.idCompte = :idCompte " +
                    "AND t.validate = true " +
                    "AND t.compte.client.idClient = :idClient ";
        } else {

            jpql = "SELECT SUM(CASE WHEN t.sens = 'credit' THEN t.montant ELSE -t.montant END) " +
                    "FROM TransactionCourant t " +
                    "WHERE t.compte.idCompte = :idCompte " +
                    "AND t.compte.client.idClient = :idClient " +
                    "AND t.validate = true " +
                    "AND t.dateTransaction <= :dateTransaction";
        }

        Query query = em.createQuery(jpql);
        query.setParameter("idCompte", idCompte);
        query.setParameter("idClient", idClient);
        if (dateTransaction != null) {
            query.setParameter("dateTransaction", dateTransaction);
        }

        Double solde = (Double) query.getSingleResult();
        return solde != null ? solde : 0.0;
    }

    public ClientCourant findById(Integer id) {
        return em.find(ClientCourant.class, id);
    }

    public List<ClientCourant> findAll() {
        TypedQuery<ClientCourant> query = em.createQuery(
                "SELECT c FROM ClientCourant c", ClientCourant.class);
        return query.getResultList();
    }

    public ClientCourant findByEmail(String email) {
        try {
            return em.createQuery(
                    "SELECT c FROM ClientCourant c WHERE c.email = :email",
                    ClientCourant.class).setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null; 
        }
    }

    public List<ClientCourant> findByNom(String nom) {
        TypedQuery<ClientCourant> query = em.createQuery(
                "SELECT c FROM ClientCourant c WHERE c.nom = :nom", ClientCourant.class);
        query.setParameter("nom", nom);
        return query.getResultList();
    }

    public List<ClientCourant> findByDateNaissanceBefore(LocalDate date) {
        TypedQuery<ClientCourant> query = em.createQuery(
                "SELECT c FROM ClientCourant c WHERE c.dateNaissance < :date", ClientCourant.class);
        query.setParameter("date", date);
        return query.getResultList();
    }

    public void save(ClientCourant client) {
        if (client.getIdClient() == null) {
            em.persist(client);
        } else {
            em.merge(client);
        }
    }

    public void delete(ClientCourant client) {
        em.remove(em.contains(client) ? client : em.merge(client));
    }
}
