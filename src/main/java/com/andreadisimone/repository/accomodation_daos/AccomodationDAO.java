package com.andreadisimone.repository.accomodation_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.accomodation_dtos.AccomodationRequestDTO;
import com.andreadisimone.dtos.accomodation_dtos.AccomodationResponseDTO;
import com.andreadisimone.model.Accomodation;
import com.andreadisimone.model.Host;

public interface AccomodationDAO {
    

    // ==================== CREATE ====================

    AccomodationResponseDTO create(AccomodationRequestDTO accomodation);

    // ==================== READ ====================
    List<Accomodation> findAll();

    List<Accomodation> findAllHostAccomodations(Host idHost);

    Optional<Accomodation> findById(Integer idAccomodation);

    // ==================== UPDATE ====================

    AccomodationResponseDTO update(AccomodationResponseDTO accomodation, Integer idAccomodation);

    // ==================== DELETE ====================

    int deleteAllHostAccomodations(Host idHost);
    
    boolean deleteById(Integer idAccomodation);

    
}

    

