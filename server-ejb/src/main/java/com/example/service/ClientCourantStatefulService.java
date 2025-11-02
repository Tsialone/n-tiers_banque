package com.example.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.mappers.ActionRoleMapper;
import com.example.mappers.ClientCourantMapper;
import com.example.mappers.ClientRoleMapper;
import com.example.mappers.DirectionMapper;
import com.example.mappers.RoleMapper;
import com.example.models.ActionRole;
import com.example.models.ClientCourant;
import com.example.models.ClientRole;
import com.example.models.Role;
import com.example.remotes.ClientCourantStatefulServiceRemote;
import com.example.repositories.ClientCourantRepository;
import com.example.repositories.DirectionRepository;
import com.example.repositories.RoleRepository;
import com.example.server_dtos.ActionRoleDto;
import com.example.server_dtos.ClientCourantDto;
import com.example.server_dtos.ClientRoleDto;
import com.example.server_dtos.DirectionDto;

@Stateful
@Remote(ClientCourantStatefulServiceRemote.class)
public class ClientCourantStatefulService implements ClientCourantStatefulServiceRemote, Serializable {

    @EJB
    private ClientCourantRepository repository;

    // @EJB
    // private Rolerep repository;

    @EJB
    private DirectionRepository directionRepository;

    @EJB
    private RoleRepository roleRepository;

    private ClientCourantDto client;
    private DirectionDto directionDto;
    private List<ActionRoleDto> actionRoleDtos = new ArrayList<>();

    @Override
    public List<ActionRoleDto> getActionRoles() {
        return actionRoleDtos;
    }

    @Override
    public DirectionDto getDirection() {
        return directionDto;
    }

    public void setDirection(DirectionDto directionDto) {
        this.directionDto = directionDto;
    }

    @Override
    public ClientCourantDto getClient() {
        return client;
    }

    // @Remove
    // public void logout() {
    // this.client = null;
    // }

    @Override
    public ClientCourantDto login(String email, String mdp) throws Exception {
        try {
            ClientCourant clientCourant = repository.findByEmail(email);
            ClientCourantDto clientCourantDto = ClientCourantMapper.toDto(repository.findByEmail(email));

            if (clientCourant == null)
                throw new Exception("Email non trouver");
            if (!clientCourant.getMdp().equals(mdp))
                throw new Exception("Mot de passe incorrect");

            DirectionDto directionDto = DirectionMapper
                    .toDto(clientCourant.getDirection());

            for (ClientRoleDto clientRole : clientCourantDto.getClientRoles()) {
                Role role = roleRepository.findById(clientRole.getIdRole());
                for (ActionRole actionRole : role.getActionRoles()) {
                    ActionRoleDto actionRoleDto = ActionRoleMapper.toDto(actionRole);
                    actionRoleDto.setLibelleAction(actionRole.getAction().getLibelle());
                    actionRoleDto.setLibelleRole(actionRole.getRole().getLibelle());

                    actionRoleDtos.add(actionRoleDto);
                }
            }
            // else {
            // clientCourant.getComptes().size();
            // clientCourant.getClientRoles().size();
            // for (ClientRole clientRole : clientCourant.getClientRoles()) {
            // clientRole.getRole().getActionRoles().size();
            // }
            // }
            this.setClient(clientCourantDto);
            this.setDirection(directionDto);

            return clientCourantDto;
        } catch (Exception e) {
            throw e;
        }
    }

    public void setClient(ClientCourantDto client) {
        this.client = client;
    }

    @Override
    public void logout() {
        client = null;
        actionRoleDtos.clear();
        directionDto = null;
    }

}
