package com.example.service;

import com.example.mappers.ClientCourantMapper;
import com.example.models.ClientCourant;
import com.example.models.ClientRole;
import com.example.repositories.ClientCourantRepository;
import com.example.repositories.DirectionRepository;
import com.example.server_dtos.ClientCourantDto;
import com.example.server_dtos.CompteCourantDto;

import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.models.CompteCourant;
import com.example.models.Direction;
import com.example.remotes.ClientCourantServiceRemote;
import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.remotes.CompteCourantServiceRemote;

@Stateless
@Remote(ClientCourantServiceRemote.class)
public class ClientCourantService implements ClientCourantServiceRemote {

    @EJB
    private ClientCourantRepository repository;

    @EJB
    private CompteCourantServiceRemote compteCourantService;

    @EJB
    private DirectionRepository directionRepository;


    // @EJB
    // private ClientCourantStatefulServiceRemote
    // clientCourantStatefulServiceRemote;

    // public ClientCourant getUtilisateur() throws Exception {

    // try {
    // return clientCourantStatefulService.getClient();
    // } catch (Exception e) {
    // throw e;
    // }
    // }

    // public ClientCourant login(Integer idClient) {

    // try {
    // ClientCourant clientCourant = getClientById(idClient);
    // clientCourant.getComptes().size();
    // clientCourantStatefulService.setClient(clientCourant);
    // return clientCourant;
    // } catch (Exception e) {
    // throw e;
    // }
    // }

    // Récupérer un client par son ID
    @Override
    public ClientCourantDto getClientById(Integer idClient) {
        ClientCourantDto client = ClientCourantMapper.toDto(repository.findById(idClient));
        if (client == null) {
            throw new IllegalArgumentException("Client non trouvé : " + idClient);
        }
        return client;
    }

    // Lister tous les clients
    @Override
    public List<ClientCourantDto> getAllClients() {
        return repository.findAll()
                .stream()
                .map(ClientCourantMapper::toDto)
                .collect(Collectors.toList());
    }

    // Chercher par nom
    @Override
    public List<ClientCourantDto> getClientsByNom(String nom) {
        return repository.findByNom(nom)
                .stream()
                .map(ClientCourantMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public ClientCourantDto saveClient(ClientCourantDto client) {
        Direction direction =  directionRepository.findById(client.getIdDirection());
        repository.save(ClientCourantMapper.toEntity(client , direction));
        return client;
    }

    @Override
    public void deleteClient(ClientCourantDto client) {
        ClientCourant clientCourant  = repository.findById(client.getIdClient());
        repository.delete(clientCourant);
    }

    public ClientCourant getByEmail(String email) throws Exception {
        try {
            return repository.findByEmail(email);
        } catch (Exception e) {
            throw e;
        }
    }

    // getSoldeByIdClientAndIdCompte
    public double getSoldeByIdClientAndIdCompte(Integer idCompte, Integer idClient, LocalDate dateTransaction)
            throws Exception {
        try {

            CompteCourantDto compteCourant = compteCourantService.getCompteById(idCompte);
            return repository.findSoldeByIdClientAndIdCompte(idCompte, idClient, dateTransaction)
                    + compteCourant.getCapital();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
