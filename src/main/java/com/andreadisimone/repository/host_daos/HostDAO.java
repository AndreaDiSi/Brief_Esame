package com.andreadisimone.repository.host_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.host_dtos.HostRequestDTO;
import com.andreadisimone.dtos.host_dtos.HostResponseDTO;

public interface HostDAO {
    

    // ==================== CREATE ====================

    HostResponseDTO create(HostRequestDTO request);
    
    // ==================== READ ====================

    List<HostResponseDTO> findAll();

    Optional<HostResponseDTO> findById(Integer idHost);

    HostResponseDTO getBestHost();

    List<HostResponseDTO> getTopFiveBestHost();

    List<HostResponseDTO> getAllSuperHosts();
    // ==================== UPDATE ====================

    HostResponseDTO update(Integer id, HostResponseDTO request);

    // ==================== DELETE ====================


    boolean deleteById(Integer idHost);

    

}
