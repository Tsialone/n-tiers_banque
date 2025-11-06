package com.example.remotes;

import java.time.LocalDate;
import java.util.List;

import com.example.server_dtos.VirementDto;

import jakarta.ejb.Remote;

@Remote
public interface ValidationTransactionServiceRemote {

 public boolean saveValidation(Integer idTransaction , Integer idUtilisateur, String action , boolean doubleChangeControl) throws Exception ;
 
    // ValidationDto saveValidation(Integer idTransactionCourant, String etat) throws Exception;
}
