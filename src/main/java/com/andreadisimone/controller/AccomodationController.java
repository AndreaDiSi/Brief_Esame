package com.andreadisimone.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andreadisimone.dtos.accomodation_dtos.AccomodationRequestDTO;
import com.andreadisimone.dtos.accomodation_dtos.AccomodationResponseDTO;
import com.andreadisimone.model.Accomodation;
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
    }

    //====================CREATE==========================//
    public void createAccomodation(Context ctx) {
        log.info("POST /api/v1/accomodations - Request to create an accomodation");

        // legge l'http request(JSON) e lo converte in un AccomodationRequest java
        AccomodationRequestDTO accomodation = ctx.bodyAsClass(AccomodationRequestDTO.class);

        //
        try {
            accomodationService.insertAccomodation(accomodation);

            log.info("Accomodation successfully created - name: {}", accomodation.getAccomodationName());
            ctx.status(HttpStatus.CREATED);
            ctx.json(accomodation);

        } catch (Exception ex) {
            log.warn("Creazione fallita - {}", ex.getMessage());
            ctx.status(HttpStatus.CONFLICT); // 409
            ctx.json(buildErrorResponse(ex.getMessage()));
        }
    }

    //====================READ==========================//
    public void getAllAccomodations(Context ctx) {
        log.info("GET /api/v1/accomodations - Request to show all accomodations");
        List<Accomodation> accomodationList = accomodationService.getAllAccomodation();
        ctx.status(HttpStatus.OK);
        ctx.json(accomodationList);
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

    private Map<String, String> buildErrorResponse(String errorMessage) {
        Map<String, String> error2return = new HashMap<>();
        error2return.put("error", errorMessage);
        return error2return;
    }

    private Map<String, String> buildSuccessResponse(String message) {
        Map<String, String> success2return = new HashMap<>();
        success2return.put("message", message);
        return success2return;
    }

}
