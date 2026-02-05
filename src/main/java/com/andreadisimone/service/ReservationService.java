package com.andreadisimone.service;

import java.time.LocalDate;
import java.util.List;

import com.andreadisimone.dtos.reservation_dtos.ReservationRequestDTO;
import com.andreadisimone.dtos.reservation_dtos.ReservationResponseDTO;
import com.andreadisimone.exceptions.not_found.ReservationNotFoundException;
import com.andreadisimone.repository.reservation_daos.ReservationDAO;
import com.andreadisimone.repository.reservation_daos.ReservationDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReservationService {

    private final ReservationDAO reservationDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAOImpl();
    }

    public ReservationResponseDTO insert(ReservationRequestDTO request) {
        log.info("Attempting to create a new reservation for Tenant ID: {} and Accomodation ID: {}",
                request.getIdTenant(), request.getIdAccomodation());

        validateReservationInput(request);

        try {
            return reservationDAO.create(request);
        } catch (Exception e) {
            log.error("Error creating reservation: {}", e.getMessage());
            throw new RuntimeException("Could not create reservation: " + e.getMessage());
        }
    }

    public List<ReservationResponseDTO> getAll() {
        log.info("Fetching all reservations from database");
        return reservationDAO.findAll();
    }

    public boolean delete(Integer idReservation) {
        log.info("Attempting to delete reservation with ID: {}", idReservation);

        if (idReservation == null || idReservation <= 0) {
            log.error("Invalid reservation ID: {}", idReservation);
            throw new IllegalArgumentException("Reservation ID must be positive");
        }

        boolean deleted = reservationDAO.deleteById(idReservation);
        if (!deleted) {
            log.warn("Reservation with ID: {} not found for deletion", idReservation);
            throw new ReservationNotFoundException("Reservation with ID " + idReservation + " not found");
        }
        return deleted;
    }

    public ReservationResponseDTO update(Integer idReservation, ReservationRequestDTO request) {
        log.info("Attempting to update reservation with ID: {}", idReservation);

        if (idReservation == null || idReservation <= 0) {
            log.error("Invalid reservation ID: {}", idReservation);
            throw new IllegalArgumentException("Reservation ID must be positive");
        }

        validateReservationUpdateInput(request);

        ReservationResponseDTO updated = reservationDAO.update(idReservation, request);
        if (updated == null) {
            log.error("Reservation with ID: {} not found", idReservation);
            throw new ReservationNotFoundException("Reservation with ID " + idReservation + " not found");
        }
        return updated;
    }

    public ReservationResponseDTO findById(Integer idReservation) {
        log.info("Fetching reservation with ID: {}", idReservation);

        if (idReservation == null || idReservation <= 0) {
            log.error("Invalid reservation ID: {}", idReservation);
            throw new IllegalArgumentException("Reservation ID must be positive");
        }

        return reservationDAO.findById(idReservation)
                .orElseThrow(() -> {
                    log.error("Reservation with ID: {} not found", idReservation);
                    return new ReservationNotFoundException("Reservation with ID " + idReservation + " not found");
                });
    }

    private void validateReservationInput(ReservationRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Reservation data cannot be null");
        }

        if (request.getIdTenant() == null || request.getIdTenant() <= 0) {
            log.error("Invalid tenant ID: {}", request.getIdTenant());
            throw new IllegalArgumentException("Tenant ID must be positive");
        }

        if (request.getIdAccomodation() == null || request.getIdAccomodation() <= 0) {
            log.error("Invalid accommodation ID: {}", request.getIdAccomodation());
            throw new IllegalArgumentException("Accommodation ID must be positive");
        }

        if (request.getReservationStartDate() == null) {
            log.error("Reservation start date is null");
            throw new IllegalArgumentException("Reservation start date cannot be null");
        }

        if (request.getReservationEndDate() == null) {
            log.error("Reservation end date is null");
            throw new IllegalArgumentException("Reservation end date cannot be null");
        }

        if (request.getReservationEndDate().isBefore(request.getReservationStartDate())) {
            log.error("Validation failed: End date {} is before start date {}", 
                     request.getReservationEndDate(), request.getReservationStartDate());
            throw new IllegalArgumentException("Reservation end date must be after start date");
        }

        if (request.getReservationStartDate().isBefore(LocalDate.now())) {
            log.error("Reservation start date {} is in the past", request.getReservationStartDate());
            throw new IllegalArgumentException("Reservation start date cannot be in the past");
        }

        // Controlla che la prenotazione non sia troppo lunga (es. max 1 anno)
        if (request.getReservationStartDate().plusYears(1).isBefore(request.getReservationEndDate())) {
            log.error("Reservation duration exceeds 1 year");
            throw new IllegalArgumentException("Reservation duration cannot exceed 1 year");
        }
    }

    private void validateReservationUpdateInput(ReservationRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Reservation data cannot be null");
        }

        if (request.getReservationStartDate() != null && request.getReservationEndDate() != null) {
            if (request.getReservationEndDate().isBefore(request.getReservationStartDate())) {
                log.error("Reservation end date must be after start date");
                throw new IllegalArgumentException("Reservation end date must be after start date");
            }
        }
    }
}