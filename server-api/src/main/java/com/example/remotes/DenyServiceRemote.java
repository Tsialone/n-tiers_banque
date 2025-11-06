package com.example.remotes;

import com.example.change_dtos.DeviseDto;

import jakarta.ejb.Remote;

@Remote
public interface DenyServiceRemote  {
    
     public boolean denyAllOperations(DeviseDto deviseDto, Integer idUtilisateur) throws Exception;
}
