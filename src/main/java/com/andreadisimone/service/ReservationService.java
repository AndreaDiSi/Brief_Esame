package com.andreadisimone.service;

import java.util.List;

import com.andreadisimone.dtos.reservation_dtos.ReservationRequestDTO;
import com.andreadisimone.dtos.reservation_dtos.ReservationResponseDTO;
import com.andreadisimone.exceptions.not_found.ReservationNotFoundException; // Da creare
import com.andreadisimone.repository.reservation_daos.ReservationDAO;
import com.andreadisimone.repository.reservation_daos.ReservationDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReservationService {

    private final ReservationDAO reservationDAO;

    public ReservationService() {
        // Dependency inversion: inizializzo l'implementazione
        this.reservationDAO = new ReservationDAOImpl();
    }

    public ReservationResponseDTO insert(ReservationRequestDTO request) {
        log.info("Attempting to create a new reservation for Tenant ID: {} and Accomodation ID: {}", 
                 request.getIdTenant(), request.getIdAccomodation());

        // Validazione logica di business (End Date > Start Date)
        if (request.getReservationEndDate().isBefore(request.getReservationStartDate())) {
            log.error("Validation failed: End date is before start date");
            throw new IllegalArgumentException("Reservation end date must be after start date");
        }

        try {
            return reservationDAO.create(request);
        } catch (Exception e) {
            log.error("Error during reservation creation: {}", e.getMessage());
            throw new RuntimeException("Could not complete reservation: " + e.getMessage());
        }
    }

    public List<ReservationResponseDTO> getAll() {
        log.info("Fetching all reservations from database");
        return reservationDAO.findAll();
    }

    public boolean delete(Integer idReservation) {
        log.info("Attempting to delete reservation with ID: {}", idReservation);
        
        boolean deleted = reservationDAO.deleteById(idReservation);
        if (!deleted) {
            log.warn("Reservation with ID: {} not found for deletion", idReservation);
        }
        return deleted;
    }

    public ReservationResponseDTO update(Integer idReservation, ReservationRequestDTO request) {
        log.info("Attempting to update reservation with ID: {}", idReservation);

        // Validazione date anche in fase di update
        if (request.getReservationEndDate().isBefore(request.getReservationStartDate())) {
            throw new IllegalArgumentException("Reservation end date must be after start date");
        }

        try {
            ReservationResponseDTO updated = reservationDAO.update(idReservation, request);
            if (updated == null) {
                throw new ReservationNotFoundException("Reservation with ID " + idReservation + " not found");
            }
            return updated;
        } catch (Exception e) {
            log.error("Update failed for reservation ID {}: {}", idReservation, e.getMessage());
            throw e;
        }
    }
    
    public ReservationResponseDTO findById(Integer idReservation) {
        log.info("Fetching reservation with ID: {}", idReservation);

        return reservationDAO.findById(idReservation)
                .orElseThrow(() -> {
                    log.error("Reservation with ID: {} not found", idReservation);
                    return new ReservationNotFoundException("Reservation with ID " + idReservation + " not found");
                });
    }
}