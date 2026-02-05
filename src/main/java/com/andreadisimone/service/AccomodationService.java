package com.andreadisimone.service;

import java.time.LocalDate;
import java.util.List;

import com.andreadisimone.dtos.accomodation_dtos.AccomodationRequestDTO;
import com.andreadisimone.dtos.accomodation_dtos.AccomodationResponseDTO;
import com.andreadisimone.exceptions.not_found.AccomodationNotFoundException;
import com.andreadisimone.exceptions.not_found.HostNotFoundException;
import com.andreadisimone.repository.accomodation_daos.AccomodationDAO;
import com.andreadisimone.repository.accomodation_daos.AccomodationDAOImpl;
import com.andreadisimone.repository.host_daos.HostDAO;
import com.andreadisimone.repository.host_daos.HostDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccomodationService {
    
    private final AccomodationDAO accomodationDao;
    private final HostDAO hostDao;

    public AccomodationService(){
        accomodationDao = new AccomodationDAOImpl();
        hostDao = new HostDAOImpl();
    }

    public AccomodationResponseDTO insertAccomodation(AccomodationRequestDTO accomodation){
        log.info("Trying to insert Accomodation - name: {}", accomodation.getAccomodationName());

        // Validazione input
        validateAccomodationInput(accomodation);

        try{
            // Verifica esistenza host
            hostDao.findById(accomodation.getHostId());
            log.info("Host with id {} found", accomodation.getHostId());

            AccomodationRequestDTO accomodation2save = new AccomodationRequestDTO(
                accomodation.getAccomodationName(), 
                accomodation.getNRooms(), 
                accomodation.getAccomodationAddress(), 
                accomodation.getNBedPlaces(), 
                accomodation.getFloor(), 
                accomodation.getPrice(), 
                accomodation.getEndDate(), 
                accomodation.getStartDate(), 
                accomodation.getHostId()
            );
            
            return accomodationDao.create(accomodation2save);
            
        } catch (HostNotFoundException e) {
            log.error("Host with id {} not found", accomodation.getHostId());
            throw new HostNotFoundException("Host with id " + accomodation.getHostId() + " not found");
        }
    }

    public List<AccomodationResponseDTO> getAllAccomodation(){
        log.info("Trying to get all Accomodations");
        return accomodationDao.getAll();
    }

    public AccomodationResponseDTO getBestAccomodation(){
        log.info("Trying to get the best Accomodation");
        
        AccomodationResponseDTO best = accomodationDao.getBestAccomodation();
        if (best == null) {
            log.warn("No accommodations found in database");
            throw new AccomodationNotFoundException("No accommodations available");
        }
        return best;
    }

    public boolean deleteById(Integer idAccomodation){
        log.info("Trying to delete Accomodation with id: {}", idAccomodation);

        if (idAccomodation == null || idAccomodation <= 0) {
            log.error("Invalid accomodation ID: {}", idAccomodation);
            throw new IllegalArgumentException("Accomodation ID must be positive");
        }

        boolean deleted = accomodationDao.deleteById(idAccomodation);
        if (!deleted) {
            log.warn("Accomodation with id {} not found for deletion", idAccomodation);
            throw new AccomodationNotFoundException("Accomodation with id " + idAccomodation + " not found");
        }
        return deleted;
    }

    public AccomodationResponseDTO update(Integer idAccomodation, AccomodationResponseDTO accomodation){
        log.info("Trying to update Accomodation with id: {}", idAccomodation);

        if (idAccomodation == null || idAccomodation <= 0) {
            log.error("Invalid accomodation ID: {}", idAccomodation);
            throw new IllegalArgumentException("Accomodation ID must be positive");
        }

        validateAccomodationUpdateInput(accomodation);

        accomodation.setIdAccomodation(idAccomodation);

        AccomodationResponseDTO updated = accomodationDao.update(accomodation, idAccomodation);
        if (updated == null) {
            log.error("Accomodation with id {} not found for update", idAccomodation);
            throw new AccomodationNotFoundException("Accomodation with id " + idAccomodation + " not found");
        }
        return updated;
    }

    private void validateAccomodationInput(AccomodationRequestDTO accomodation) {
        if (accomodation == null) {
            throw new IllegalArgumentException("Accomodation data cannot be null");
        }

        if (accomodation.getAccomodationName() == null || accomodation.getAccomodationName().trim().isEmpty()) {
            log.error("Accomodation name is empty");
            throw new IllegalArgumentException("Accomodation name cannot be empty");
        }

        if (accomodation.getAccomodationAddress() == null || accomodation.getAccomodationAddress().trim().isEmpty()) {
            log.error("Accomodation address is empty");
            throw new IllegalArgumentException("Accomodation address cannot be empty");
        }

        if (accomodation.getNRooms() == null || accomodation.getNRooms() <= 0) {
            log.error("Invalid number of rooms: {}", accomodation.getNRooms());
            throw new IllegalArgumentException("Number of rooms must be positive");
        }

        if (accomodation.getNBedPlaces() == null || accomodation.getNBedPlaces() <= 0) {
            log.error("Invalid number of bed places: {}", accomodation.getNBedPlaces());
            throw new IllegalArgumentException("Number of bed places must be positive");
        }

        if (accomodation.getPrice() == null || accomodation.getPrice() <= 0) {
            log.error("Invalid price: {}", accomodation.getPrice());
            throw new IllegalArgumentException("Price must be positive");
        }

        // Validazione date
        if (accomodation.getStartDate() != null && accomodation.getEndDate() != null) {
            if (accomodation.getEndDate().isBefore(accomodation.getStartDate())) {
                log.error("End date {} is before start date {}", accomodation.getEndDate(), accomodation.getStartDate());
                throw new IllegalArgumentException("End date must be after start date");
            }
            
            if (accomodation.getStartDate().isBefore(LocalDate.now())) {
                log.error("Start date {} is in the past", accomodation.getStartDate());
                throw new IllegalArgumentException("Start date cannot be in the past");
            }
        }
    }

    private void validateAccomodationUpdateInput(AccomodationResponseDTO accomodation) {
        if (accomodation == null) {
            throw new IllegalArgumentException("Accomodation data cannot be null");
        }

        if (accomodation.getAccomodationName() != null && accomodation.getAccomodationName().trim().isEmpty()) {
            throw new IllegalArgumentException("Accomodation name cannot be empty");
        }

        if (accomodation.getPrice() != null && accomodation.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        if (accomodation.getNRooms() != null && accomodation.getNRooms() <= 0) {
            throw new IllegalArgumentException("Number of rooms must be positive");
        }
    }
}