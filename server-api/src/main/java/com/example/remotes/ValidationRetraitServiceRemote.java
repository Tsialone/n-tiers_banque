package com.example.remotes;

import jakarta.ejb.Remote;

@Remote
public interface ValidationRetraitServiceRemote {
    boolean saveValidation(Integer idRetrait, Double newMontant  ,  Double newTaux , String action, boolean doubleChangeControl) throws Exception;
}
