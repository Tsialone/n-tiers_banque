package com.example.remotes;

import jakarta.ejb.Remote;
import java.util.List;

import com.example.server_dtos.ActionRoleDto;
import com.example.server_dtos.ClientCourantDto;
import com.example.server_dtos.DirectionDto;

@Remote
public interface ClientCourantStatefulServiceRemote {
    void logout ();
    List<ActionRoleDto> getActionRoles();

    DirectionDto getDirection();

    ClientCourantDto login(String email, String mdp) throws Exception;

    // Sélectionner le client courant pour la session
    void setClient(ClientCourantDto client);

    // Récupérer le client courant
    ClientCourantDto getClient() throws Exception;

}
