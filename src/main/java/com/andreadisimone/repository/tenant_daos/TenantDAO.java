package com.andreadisimone.repository.tenant_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.reservation_dtos.ReservationResponseDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantRequestDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantResponseDTO;

public interface TenantDAO {
    
    // ==================== CREATE ====================
    TenantResponseDTO create(TenantRequestDTO request);
    
    // ==================== READ ====================
    List<TenantResponseDTO> findAll();

    Optional<TenantResponseDTO> findById(Integer idTenant);
    
    Optional<ReservationResponseDTO> getLastReservation(Integer idTenant);
    // ==================== UPDATE ====================
    TenantResponseDTO update(Integer id, TenantRequestDTO request);

    // ==================== DELETE ====================
    boolean deleteById(Integer idTenant);
}