package com.andreadisimone.service;

import java.util.List;

import com.andreadisimone.dtos.reservation_dtos.ReservationResponseDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantRequestDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantResponseDTO;
import com.andreadisimone.exceptions.not_found.ReservationNotFoundException;
import com.andreadisimone.exceptions.not_found.TenantNotFoundException;
import com.andreadisimone.repository.tenant_daos.TenantDAO;
import com.andreadisimone.repository.tenant_daos.TenantDAOImpl; // Assicurati di creare questa eccezione

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantService {

    // Dependency inversion per principi SOLID
    private final TenantDAO tenantDAO;

    public TenantService() {
        // Inizializzazione dell'implementazione
        this.tenantDAO = new TenantDAOImpl();
    }

    public TenantResponseDTO insertTenant(TenantRequestDTO tenantRequest) {
        log.info("Trying to insert tenant - name: {}", tenantRequest.getTenantName());

        try {
            return tenantDAO.create(tenantRequest);
        } catch (Exception e) {
            log.error("Error creating tenant: {}", tenantRequest.getTenantName());

            throw new RuntimeException("Could not create tenant: " + e.getMessage());
        }
    }

    public List<TenantResponseDTO> getAllTenants() {
        log.info("Trying to get all tenants");
        return tenantDAO.findAll();
    }

    public boolean deleteById(Integer idTenant) {
        log.info("Trying to delete tenant with id: {}", idTenant);
        return tenantDAO.deleteById(idTenant);
    }

    public TenantResponseDTO update(Integer idTenant, TenantRequestDTO tenantRequest) {
        log.info("Trying to update tenant with id: {}", idTenant);

        try {
            return tenantDAO.update(idTenant, tenantRequest);
        } catch (TenantNotFoundException e) {
            log.error("Tenant with id {} not found for update", idTenant);
            throw e;
        }
    }

    public List<TenantResponseDTO> getTopFiveTenants() {
        log.info("Trying to get the top five best tenants");

        try {
            return tenantDAO.getTopFiveTenants();
        } catch (TenantNotFoundException e) {
            log.error("top 5 Tenant not found ");
            throw e;
        }
    }

    public <Optional> ReservationResponseDTO getLastReservation(Integer idTenant) {
        log.info("Trying to get last reservation for tenant with id: {}", idTenant);

        return tenantDAO.getLastReservation(idTenant).orElseThrow(() -> {
            return new ReservationNotFoundException("Last Reservation not found");
        });

    }
}
