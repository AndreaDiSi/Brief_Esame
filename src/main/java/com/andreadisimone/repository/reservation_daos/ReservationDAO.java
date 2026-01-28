package com.andreadisimone.repository.reservation_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.model.Reservation;
import com.andreadisimone.model.Tenant;

public interface ReservationDAO {

    // ==================== CREATE ====================

    Reservation create(Reservation Reservation);

    // ==================== READ ====================
    List<Reservation> findAll();

    List<Reservation> findAllTenantReservations(Tenant idTenant);

    Optional<Reservation> findById(Integer idReservation);

    // ==================== UPDATE ====================

    Optional<Reservation> update(Reservation Reservation);

    // ==================== DELETE ====================

    int deleteAllTenantReservations(Tenant idTenant);
    
    boolean deleteById(Integer idReservation);

    
}