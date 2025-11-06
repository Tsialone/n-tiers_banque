package com.example.remotes;


import jakarta.ejb.Remote;
import java.util.List;

import com.example.server_dtos.DepotDto;

@Remote
public interface DepotServiceRemote {
    // Depot findById(Integer id) throws Exception;
    // List<Depot> findAll() throws Exception;
    void create(DepotDto depot) throws Exception;
    // Depot update(Depot depot) throws Exception;
    // void delete(Depot depot) throws Exception;
    List<DepotDto> findByCompte(Integer idCompte) throws Exception;
}
