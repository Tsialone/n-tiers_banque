package com.example.remotes;

import java.time.LocalDate;
import java.util.List;

import com.example.change_dtos.DeviseDto;
import com.example.server_dtos.VirementDto;

import jakarta.ejb.Remote;

@Remote
public interface VirementServiceRemote {
  // public boolean denyAllOperations(DeviseDto deviseDto) ;
    public double alleas  (VirementDto vdDto) throws Exception;
  public List<VirementDto> getVirementsByCompteDebitAndCompteCredit(Integer idCompte) throws Exception;

  public VirementDto effectuerVirement(
      VirementDto virementDto, Double tauxEchange) throws Exception;

  // ValidationDto saveValidation(Integer idTransactionCourant, String etat)
  // throws Exception;
}
