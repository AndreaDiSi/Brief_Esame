package com.andreadisimone.repository.accomodation_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.model.Accomodation;
import com.andreadisimone.model.Host;

public interface AccomodationDAO {
    

    // ==================== CREATE ====================

    Accomodation create(Accomodation accomodation);

    // ==================== READ ====================
    List<Accomodation> findAll();

    List<Accomodation> findAllHostAccomodations(Host idHost);

    Optional<Accomodation> findById(Integer idAccomodation);

    // ==================== UPDATE ====================

    Optional<Accomodation> update(Accomodation accomodation);

    // ==================== DELETE ====================

    int deleteAllHostAccomodations(Host idHost);
    boolean deleteById(Integer idAccomodation);

    
}

    

