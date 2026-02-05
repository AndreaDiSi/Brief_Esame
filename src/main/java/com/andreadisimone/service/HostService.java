package com.andreadisimone.service;

import java.util.List;
import java.util.regex.Pattern;

import com.andreadisimone.dtos.host_dtos.HostRequestDTO;
import com.andreadisimone.dtos.host_dtos.HostResponseDTO;
import com.andreadisimone.exceptions.not_found.HostNotFoundException;
import com.andreadisimone.repository.host_daos.HostDAO;
import com.andreadisimone.repository.host_daos.HostDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HostService {

    private final HostDAO hostDAO;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public HostService() {
        hostDAO = new HostDAOImpl();
    }

    public HostResponseDTO insertHost(HostRequestDTO host) {
        log.info("Trying to insert host - name: {}", host.getHostName());

        validateHostInput(host);

        try {
            HostRequestDTO host2save = new HostRequestDTO(
                host.getHostName(), 
                host.getSurname(), 
                host.getEmail(), 
                host.getHostAddress(), 
                host.isSuperhost()
            );

            return hostDAO.create(host2save);

        } catch (Exception e) {
            log.error("Error creating host {}: {}", host.getHostName(), e.getMessage());
            throw new RuntimeException("Could not create host: " + e.getMessage());
        }
    }

    public List<HostResponseDTO> getAllhost() {
        log.info("Trying to get all hosts");
        return hostDAO.findAll();
    }

    public HostResponseDTO getBestHost() {
        log.info("Trying to get best host");
        
        HostResponseDTO best = hostDAO.getBestHost();
        if (best == null) {
            log.warn("No hosts found in database");
            throw new HostNotFoundException("No hosts available");
        }
        return best;
    }

    public List<HostResponseDTO> getAllSuperHosts() {
        log.info("Trying to get all superhosts");
        return hostDAO.getAllSuperHosts();
    }

    public boolean deleteById(Integer idhost) {
        log.info("Trying to delete host with id: {}", idhost);

        if (idhost == null || idhost <= 0) {
            log.error("Invalid host ID: {}", idhost);
            throw new IllegalArgumentException("Host ID must be positive");
        }

        boolean deleted = hostDAO.deleteById(idhost);
        if (!deleted) {
            log.warn("Host with id {} not found for deletion", idhost);
            throw new HostNotFoundException("Host with id " + idhost + " not found");
        }
        return deleted;
    }

    public HostResponseDTO update(Integer idhost, HostResponseDTO host) {
        log.info("Trying to update host with id: {}", idhost);

        if (idhost == null || idhost <= 0) {
            log.error("Invalid host ID: {}", idhost);
            throw new IllegalArgumentException("Host ID must be positive");
        }

        validateHostUpdateInput(host);

        host.setIdHost(idhost);

        HostResponseDTO updated = hostDAO.update(idhost, host);
        if (updated == null) {
            log.error("Host with id {} not found for update", idhost);
            throw new HostNotFoundException("Host with id " + idhost + " not found");
        }
        return updated;
    }

    private void validateHostInput(HostRequestDTO host) {
        if (host == null) {
            throw new IllegalArgumentException("Host data cannot be null");
        }

        if (host.getHostName() == null || host.getHostName().trim().isEmpty()) {
            log.error("Host name is empty");
            throw new IllegalArgumentException("Host name cannot be empty");
        }

        if (host.getSurname() == null || host.getSurname().trim().isEmpty()) {
            log.error("Host surname is empty");
            throw new IllegalArgumentException("Host surname cannot be empty");
        }

        if (host.getEmail() == null || host.getEmail().trim().isEmpty()) {
            log.error("Host email is empty");
            throw new IllegalArgumentException("Host email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(host.getEmail()).matches()) {
            log.error("Invalid email format: {}", host.getEmail());
            throw new IllegalArgumentException("Invalid email format");
        }

        if (host.getHostAddress() == null || host.getHostAddress().trim().isEmpty()) {
            log.error("Host address is empty");
            throw new IllegalArgumentException("Host address cannot be empty");
        }
    }

    private void validateHostUpdateInput(HostResponseDTO host) {
        if (host == null) {
            throw new IllegalArgumentException("Host data cannot be null");
        }

        if (host.getHostName() != null && host.getHostName().trim().isEmpty()) {
            throw new IllegalArgumentException("Host name cannot be empty");
        }

        if (host.getSurname() != null && host.getSurname().trim().isEmpty()) {
            throw new IllegalArgumentException("Host surname cannot be empty");
        }

        if (host.getEmail() != null) {
            if (host.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Host email cannot be empty");
            }
            if (!EMAIL_PATTERN.matcher(host.getEmail()).matches()) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }

        if (host.getHostAddress() != null && host.getHostAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Host address cannot be empty");
        }
    }
}