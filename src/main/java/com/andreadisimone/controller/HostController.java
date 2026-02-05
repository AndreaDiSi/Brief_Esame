package com.andreadisimone.controller;

import java.util.List;

import com.andreadisimone.dtos.host_dtos.HostRequestDTO;
import com.andreadisimone.dtos.host_dtos.HostResponseDTO;
import com.andreadisimone.dtos.tenant_dtos.TenantResponseDTO;
import com.andreadisimone.service.HostService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HostController {

    private final HostService hostService;

    public HostController() {
        this.hostService = new HostService();
    }

    public void registerRoutes(io.javalin.Javalin app) {
        app.get("/api/v1/hosts", this::getAllHosts);
        app.post("/api/v1/hosts", this::createHost);
        app.put("/api/v1/hosts/{id}", this::updateHost);
        app.delete("/api/v1/hosts/{id}", this::deleteHost);
        app.get("/api/v1/hosts/best", this::getBestHost);
        app.get("/api/v1/hosts/superhosts",this::getAllSuperHosts);
        
    }

    public void getAllHosts(Context ctx) {
        List<HostResponseDTO> hosts = hostService.getAllhost();
        ctx.status(HttpStatus.OK);
        ctx.json(hosts);
    }

    public void createHost(Context ctx) {
        HostRequestDTO request = ctx.bodyAsClass(HostRequestDTO.class);
        HostResponseDTO created = hostService.insertHost(request);
        ctx.status(HttpStatus.CREATED);
        ctx.json(created);
    }

    public void getBestHost(Context ctx) {
        HostResponseDTO bestHost = hostService.getBestHost();
        ctx.status(HttpStatus.OK);
        ctx.json(bestHost);
    }

    

    public void getAllSuperHosts(Context ctx) {
        List<HostResponseDTO> superHosts = hostService.getAllSuperHosts();
        ctx.status(HttpStatus.OK);
        ctx.json(superHosts);
    }


    public void updateHost(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        HostResponseDTO request = ctx.bodyAsClass(HostResponseDTO.class);
        HostResponseDTO updated = hostService.update(id, request);
        ctx.json(updated);
    }

    public void deleteHost(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean deleted = hostService.deleteById(id);

        if (deleted) {
            ctx.status(204);
        } else {
            ctx.status(404).result("Host not found");
        }
    }
}
