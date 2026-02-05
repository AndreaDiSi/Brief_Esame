package com.andreadisimone.controller;

import java.util.List;

import com.andreadisimone.dtos.accomodation_dtos.AccomodationRequestDTO;
import com.andreadisimone.dtos.accomodation_dtos.AccomodationResponseDTO;
import com.andreadisimone.service.AccomodationService;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccomodationController {

    private final AccomodationService accomodationService;

    public AccomodationController() {
        accomodationService = new AccomodationService();
    }

    public void registerRoutes(Javalin app) {
        app.post("/api/v1/accomodations", this::createAccomodation);
        app.get("/api/v1/accomodations", this::getAllAccomodations);
        app.delete("/api/v1/accomodations/{idAccomodation}", this::deleteAccomodation);
        app.put("/api/v1/accomodations/{idAccomodation}", this::updateAccomodation);
        app.get("/api/v1/bestAccomodations", this::getBestAccomodation);
    }

    //====================CREATE==========================//
    public void createAccomodation(Context ctx) {
        log.info("POST /api/v1/accomodations");

        AccomodationRequestDTO accomodationRequest = ctx.bodyAsClass(AccomodationRequestDTO.class);

        try {
            AccomodationResponseDTO createdAccomodation = accomodationService.insertAccomodation(accomodationRequest);

            log.info("Accomodation successfully created - ID: {}, name: {}",
                    createdAccomodation.getIdAccomodation(),
                    createdAccomodation.getAccomodationName());

            ctx.status(HttpStatus.CREATED);
            ctx.json(createdAccomodation); 

        } catch (Exception ex) {
            log.warn("Creazione fallita - {}", ex.getMessage());
            ctx.status(HttpStatus.CONFLICT);
            ctx.json(ex.getMessage());
        }
    }

    //====================READ==========================//
    public void getAllAccomodations(Context ctx) {
        log.info("GET /api/v1/accomodations - Request to show all accomodations");
        List<AccomodationResponseDTO> accomodationList = accomodationService.getAllAccomodation();
        ctx.status(HttpStatus.OK);
        ctx.json(accomodationList);
    }

    public void getBestAccomodation(Context ctx) {
        log.info("GET /api/v1/bestAccomodations - Request to show best accomodation");
        AccomodationResponseDTO bestAccomodation = accomodationService.getBestAccomodation();
        ctx.status(HttpStatus.OK);
        ctx.json(bestAccomodation);
    }

    //====================DELETE==========================//
    public void deleteAccomodation(Context ctx) {

        int id = Integer.parseInt(ctx.pathParam("idAccomodation"));
        boolean deleted = accomodationService.deleteById(id);

        if (deleted) {
            ctx.status(204);  // ← No Content, successo
        } else {
            ctx.status(404).result("Accomodation not found");
        }
    }

    //====================UPDATE==========================//
    public void updateAccomodation(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("idAccomodation"));
        AccomodationResponseDTO accomodation = ctx.bodyAsClass(AccomodationResponseDTO.class);
        AccomodationResponseDTO updated = accomodationService.update(id, accomodation);
        ctx.json(updated);
    }

    // UTILITY
}
