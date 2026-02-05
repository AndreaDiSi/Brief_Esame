package com.andreadisimone.service;

import java.util.List;
import java.util.regex.Pattern;

import com.andreadisimone.dtos.reservation_dtos.ReservationResponseDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantRequestDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantResponseDTO;
import com.andreadisimone.exceptions.not_found.ReservationNotFoundException;
import com.andreadisimone.exceptions.not_found.TenantNotFoundException;
import com.andreadisimone.repository.tenant_daos.TenantDAO;
import com.andreadisimone.repository.tenant_daos.TenantDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantService {

    private final TenantDAO tenantDAO;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public TenantService() {
        this.tenantDAO = new TenantDAOImpl();
    }

    public TenantResponseDTO insertTenant(TenantRequestDTO tenantRequest) {
        log.info("Trying to insert tenant - name: {}", tenantRequest.getTenantName());

        validateTenantInput(tenantRequest);

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
        
        if (idTenant == null || idTenant <= 0) {
            log.error("Invalid tenant ID: {}", idTenant);
            throw new IllegalArgumentException("Tenant ID must be positive");
        }
        
        boolean deleted = tenantDAO.deleteById(idTenant);
        if (!deleted) {
            log.warn("Tenant with id {} not found for deletion", idTenant);
            throw new TenantNotFoundException("Tenant with id " + idTenant + " not found");
        }
        return deleted;
    }

    public TenantResponseDTO update(Integer idTenant, TenantRequestDTO tenantRequest) {
        log.info("Trying to update tenant with id: {}", idTenant);

        if (idTenant == null || idTenant <= 0) {
            log.error("Invalid tenant ID: {}", idTenant);
            throw new IllegalArgumentException("Tenant ID must be positive");
        }

        validateTenantUpdateInput(tenantRequest);

        try {
            TenantResponseDTO updated = tenantDAO.update(idTenant, tenantRequest);
            if (updated == null) {
                throw new TenantNotFoundException("Tenant with id " + idTenant + " not found");
            }
            return updated;
        } catch (TenantNotFoundException e) {
            log.error("Tenant with id {} not found for update", idTenant);
            throw e;
        }
    }

    public List<TenantResponseDTO> getTopFiveTenants() {
        log.info("Trying to get the top five best tenants");

        try {
            List<TenantResponseDTO> topTenants = tenantDAO.getTopFiveTenants();
            if (topTenants == null || topTenants.isEmpty()) {
                log.warn("No tenants found for top 5 ranking");
            }
            return topTenants;
        } catch (Exception e) {
            log.error("Error fetching top 5 tenants: {}", e.getMessage());
            throw new RuntimeException("Could not fetch top tenants: " + e.getMessage());
        }
    }

    public ReservationResponseDTO getLastReservation(Integer idTenant) {
        log.info("Trying to get last reservation for tenant with id: {}", idTenant);

        if (idTenant == null || idTenant <= 0) {
            log.error("Invalid tenant ID: {}", idTenant);
            throw new IllegalArgumentException("Tenant ID must be positive");
        }

        return tenantDAO.getLastReservation(idTenant).orElseThrow(() -> {
            log.error("No reservations found for tenant with id: {}", idTenant);
            return new ReservationNotFoundException("No reservations found for tenant with id " + idTenant);
        });
    }

    private void validateTenantInput(TenantRequestDTO tenant) {
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant data cannot be null");
        }

        if (tenant.getTenantName() == null || tenant.getTenantName().trim().isEmpty()) {
            log.error("Tenant name is empty");
            throw new IllegalArgumentException("Tenant name cannot be empty");
        }

        if (tenant.getSurname() == null || tenant.getSurname().trim().isEmpty()) {
            log.error("Tenant surname is empty");
            throw new IllegalArgumentException("Tenant surname cannot be empty");
        }

        if (tenant.getEmail() == null || tenant.getEmail().trim().isEmpty()) {
            log.error("Tenant email is empty");
            throw new IllegalArgumentException("Tenant email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(tenant.getEmail()).matches()) {
            log.error("Invalid email format: {}", tenant.getEmail());
            throw new IllegalArgumentException("Invalid email format");
        }

        
    }

    private void validateTenantUpdateInput(TenantRequestDTO tenant) {
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant data cannot be null");
        }

        if (tenant.getTenantName() != null && tenant.getTenantName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant name cannot be empty");
        }

        if (tenant.getSurname() != null && tenant.getSurname().trim().isEmpty()) {
            throw new IllegalArgumentException("Tenant surname cannot be empty");
        }

        if (tenant.getEmail() != null) {
            if (tenant.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Tenant email cannot be empty");
            }
            if (!EMAIL_PATTERN.matcher(tenant.getEmail()).matches()) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }

        
    }
}