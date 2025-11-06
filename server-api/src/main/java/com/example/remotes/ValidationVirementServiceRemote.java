package com.example.remotes;

import java.time.LocalDate;
import java.util.List;

import com.example.server_dtos.VirementDto;

import jakarta.ejb.Remote;

@Remote
public interface ValidationVirementServiceRemote {
  boolean saveValidation(Integer idVirement, Double newMontant  , Double newTaux ,Integer idUtilisateur , String action , boolean doubleChangeControl) throws Exception;
}
