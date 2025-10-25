package com.example.remotes;


import jakarta.ejb.Remote;
import java.time.LocalDate;
import java.util.List;
import com.example.models.ClientCourant;

@Remote
public interface ClientCourantServiceRemote {
    // void  logout  () throws Exception;
    // ClientCourant getByEmail (String email) throws Exception;
    double getSoldeByIdClientAndIdCompte(Integer idCompte, Integer idClient, LocalDate dateTransaction) throws Exception;
    // ClientCourant login (Integer idClient);
    // ClientCourant getUtilisateur () throws Exception;
    ClientCourant getClientById(Integer idClient);
    List<ClientCourant> getAllClients();
    List<ClientCourant> getClientsByNom(String nom);
    ClientCourant saveClient(ClientCourant client);
    void deleteClient(ClientCourant client);
}
