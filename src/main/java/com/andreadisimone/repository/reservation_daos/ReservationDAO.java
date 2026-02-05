package com.andreadisimone.repository.reservation_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.reservation_dtos.ReservationRequestDTO;
import com.andreadisimone.dtos.reservation_dtos.ReservationResponseDTO;

public interface ReservationDAO {
    
    // ==================== CREATE ====================
    ReservationResponseDTO create(ReservationRequestDTO request);
    
    // ==================== READ ====================
    List<ReservationResponseDTO> findAll();

    Optional<ReservationResponseDTO> findById(Integer idReservation);

    

    // ==================== UPDATE ====================
    ReservationResponseDTO update(Integer id, ReservationRequestDTO request);

    // ==================== DELETE ====================
    boolean deleteById(Integer idReservation);
}