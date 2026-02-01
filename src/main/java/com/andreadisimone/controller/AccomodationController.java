package com.andreadisimone.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.andreadisimone.model.Accomodation;
import com.andreadisimone.service.AccomodationService;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccomodationController {
    private final AccomodationService accomodationService;

    public AccomodationController(){
        accomodationService = new AccomodationService();
    }
    public void registerRoutes(Javalin app){
        app.post("/api/v1/accomodations", this::createAccomodation);
        app.get("/api/v1/accomodations", this::getAllAccomodations);
    }

    //====================CREATE==========================//
    public void createAccomodation(Context ctx){
        log.info("POST /api/v1/accomodations - Request to create an accomodation");

        // legge l'http request(JSON) e lo converte in un Accomodation java
        Accomodation accomodation = ctx.bodyAsClass(Accomodation.class);

        try {
            accomodation = accomodationService.insertAccomodation(
                accomodation.getName(),
                accomodation.getNRooms(), 
                accomodation.getAccomodationAddress(),
                accomodation.getHost(),
                accomodation.getNBedPlaces(),
                accomodation.getFloor(),
                accomodation.getStartDate(), 
                accomodation.getEndDate(), 
                accomodation.getPrice()
            );
            log.info("Accomodation successfully created - ID: {}", accomodation.getIdAccomodation());
            ctx.status(HttpStatus.CREATED);
            ctx.json(accomodation);
        } catch (Exception ex) {
            log.warn("Creazione fallita - {}", ex.getMessage());
            ctx.status(HttpStatus.CONFLICT); // 409
            ctx.json(buildErrorResponse(ex.getMessage()));
        }
    }


    //====================READ==========================//

    public void getAllAccomodations(Context ctx){
        log.info("GET /api/v1/accomodations - Request to show all accomodations");
        List<Accomodation> accomodationList = accomodationService.getAllAccomodation();
        ctx.status(HttpStatus.OK);
        ctx.json(accomodationList);
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
