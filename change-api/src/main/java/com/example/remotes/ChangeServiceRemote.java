package com.example.remotes;

import jakarta.ejb.Remote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.change_dtos.DeviseDto;

@Remote
public interface ChangeServiceRemote {
    // public void test ();
    DeviseDto annulerDevise(Long id) throws Exception;
    DeviseDto validerDevise(Long id) throws Exception;
     DeviseDto getLastDeviseDto(String libelle, LocalDateTime date, Long excludeId) ;
    List<DeviseDto> getDistinct ();
    DeviseDto getByDateBtw(LocalDateTime transactionDate , String devise);
    DeviseDto addDevise(DeviseDto devise) throws Exception;
    List<DeviseDto> getAllDevises();
    DeviseDto getById(Long id);
    DeviseDto updateDevise(Long id, DeviseDto updated)  throws Exception ;
    boolean deleteDevise(Long id);
}
