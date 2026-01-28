package com.andreadisimone.repository.tenant_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.model.Tenant;

public interface TenantDAO {
    

    // ==================== CREATE ====================

    Tenant create(Tenant tenant);

    // ==================== READ ====================

    List<Tenant> findAllTenants(Tenant idTenant);

    Optional<Tenant> findById(Integer idTenant);

    // ==================== UPDATE ====================

    Optional<Tenant> update(Tenant Tenant);

    // ==================== DELETE ====================

    int deleteAllTenants();

    boolean deleteById(Integer idTenant);

    

}