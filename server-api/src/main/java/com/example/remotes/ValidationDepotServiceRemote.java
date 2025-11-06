package com.example.remotes;

import jakarta.ejb.Remote;

@Remote
public interface ValidationDepotServiceRemote {
    boolean saveValidation(Integer idDepot, Double newMontant , Double newTaux ,String action , boolean doubleChangeControl) throws Exception;
}
