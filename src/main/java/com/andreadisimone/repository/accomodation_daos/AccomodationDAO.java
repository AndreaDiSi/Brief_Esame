package com.andreadisimone.repository.accomodation_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.accomodation_dtos.AccomodationRequestDTO;
import com.andreadisimone.dtos.accomodation_dtos.AccomodationResponseDTO;
import com.andreadisimone.model.Host;

public interface AccomodationDAO {
    

    // ==================== CREATE ====================

    AccomodationResponseDTO create(AccomodationRequestDTO accomodation);

    // ==================== READ ====================
    List<AccomodationResponseDTO> getAll();

    List<AccomodationResponseDTO> getAllHostAccomodations(Host idHost);

    Optional<AccomodationResponseDTO> getById(Integer idAccomodation);

    AccomodationResponseDTO getBestAccomodation();

    // ==================== UPDATE ====================

    AccomodationResponseDTO update(AccomodationResponseDTO accomodation, Integer idAccomodation);

    // ==================== DELETE ====================

    int deleteAllHostAccomodations(Host idHost);
    
    boolean deleteById(Integer idAccomodation);

    
}

    

