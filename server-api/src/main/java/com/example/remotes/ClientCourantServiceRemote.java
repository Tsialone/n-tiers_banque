package com.example.remotes;


import jakarta.ejb.Remote;
import java.time.LocalDate;
import java.util.List;

import com.example.server_dtos.ClientCourantDto;
import com.example.server_dtos.DirectionDto;

@Remote
public interface ClientCourantServiceRemote {

    // void  logout  () throws Exception;
    // ClientCourant getByEmail (String email) throws Exception;
    double getSoldeByIdClientAndIdCompte(Integer idCompte, Integer idClient, LocalDate dateTransaction) throws Exception;
    // ClientCourant login (Integer idClient);
    // ClientCourant getUtilisateur () throws Exception;
    ClientCourantDto getClientById(Integer idClient);
    List<ClientCourantDto> getAllClients();
    List<ClientCourantDto> getClientsByNom(String nom);
    ClientCourantDto saveClient(ClientCourantDto client);
    void deleteClient(ClientCourantDto client);
}
