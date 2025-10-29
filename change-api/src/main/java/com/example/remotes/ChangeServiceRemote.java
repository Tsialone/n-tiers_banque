package com.example.remotes;

import com.example.dto.DeviseDto;

import jakarta.ejb.Remote;

import java.time.LocalDate;
import java.util.List;

@Remote
public interface ChangeServiceRemote {
    // public void test ();
    List<DeviseDto> getDistinct ();
    DeviseDto getByDateBtw(LocalDate transactionDate , String devise);
    DeviseDto addDevise(DeviseDto devise);
    List<DeviseDto> getAllDevises();
    DeviseDto getDevise(Long id);
    DeviseDto updateDevise(Long id, DeviseDto updated);
    boolean deleteDevise(Long id);
}
