package com.andreadisimone.controller;

import java.util.List;

import com.andreadisimone.dtos.reservation_dtos.ReservationResponseDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantRequestDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantResponseDTO;
import com.andreadisimone.service.TenantService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantController {

    private final TenantService tenantService;

    public TenantController() {
        // Assumiti che TenantService segua la stessa logica di HostService
        this.tenantService = new TenantService();
    }

    public void registerRoutes(io.javalin.Javalin app) {
        app.get("/api/v1/tenants", this::getAllTenants);
        app.post("/api/v1/tenants", this::createTenant);
        app.put("/api/v1/tenants/{id}", this::updateTenant);
        app.delete("/api/v1/tenants/{id}", this::deleteTenant);
        app.get("/api/v1/tenants/{id}/last-reservation", this::getLastReservation);
        app.get("/api/v1/tenants/topfivetenants", this::getTopFiveTenants);
    }

    public void getAllTenants(Context ctx) {
        log.info("Fetching all tenants");
        List<TenantResponseDTO> tenants = tenantService.getAllTenants();
        ctx.status(HttpStatus.OK);
        ctx.json(tenants);
    }

    public void createTenant(Context ctx) {
        log.info("Received request to create a new tenant");
        TenantRequestDTO request = ctx.bodyAsClass(TenantRequestDTO.class);
        TenantResponseDTO created = tenantService.insertTenant(request);
        ctx.status(HttpStatus.CREATED);
        ctx.json(created); 
    }

    public void getLastReservation(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("Fetching last reservation for tenant ID: {}", id);
        try {
            ReservationResponseDTO lastReservation = tenantService.getLastReservation(id);
            if (lastReservation != null) {
                ctx.status(HttpStatus.OK);
                ctx.json(lastReservation);
            } else {
                log.warn("No reservations found for tenant ID: {}", id);
                ctx.status(HttpStatus.NOT_FOUND).result("No reservations found for this tenant");
            }
        } catch (Exception e) {
            log.error("Error fetching last reservation for tenant {}: {}", id, e.getMessage());
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Internal server error");
        }
    }

    public void getTopFiveTenants(Context ctx) {
        List<TenantResponseDTO> topTenants = tenantService.getTopFiveTenants();
        log.info("Returning {} top tenants", topTenants.size());
        ctx.status(HttpStatus.CREATED);
        ctx.json(topTenants); // È buona norma restituire l'oggetto creato
        
    }

    public void updateTenant(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("Received request to update tenant with ID: {}", id);

        // Uso TenantRequestDTO qui per mappare il body in ingresso (o TenantResponseDTO se preferisci come nell'Host)
        TenantRequestDTO request = ctx.bodyAsClass(TenantRequestDTO.class);
        TenantResponseDTO updated = tenantService.update(id, request);

        if (updated != null) {
            ctx.status(HttpStatus.OK);
            ctx.json(updated);
        } else {
            ctx.status(HttpStatus.NOT_FOUND).result("Tenant not found");
        }
    }

    public void deleteTenant(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("Received request to delete tenant with ID: {}", id);

        boolean deleted = tenantService.deleteById(id);

        if (deleted) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.status(HttpStatus.NOT_FOUND).result("Tenant not found");
        }
    }
}
