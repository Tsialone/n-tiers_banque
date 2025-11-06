package com.example.remotes;

import com.example.server_dtos.RetraitDto;
import java.util.List;

import jakarta.ejb.Remote;

@Remote
public interface RetraitServiceRemote {
     public RetraitDto create(RetraitDto retraitDto) throws Exception;
    List<RetraitDto> findByCompte(Integer idCompte);
    RetraitDto findById(Integer idRetrait);
}
