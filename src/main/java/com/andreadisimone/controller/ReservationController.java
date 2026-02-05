package com.andreadisimone.controller;

import com.andreadisimone.dtos.reservation_dtos.ReservationRequestDTO;
import com.andreadisimone.service.ReservationService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController() {
        this.reservationService = new ReservationService();
    }

    public void registerRoutes(io.javalin.Javalin app) {
        app.get("/api/v1/reservations", this::getAll);
        app.post("/api/v1/reservations", this::create);
        app.put("/api/v1/reservations/{id}", this::update);
        app.delete("/api/v1/reservations/{id}", this::delete);
    }

    public void getAll(Context ctx) {
        ctx.json(reservationService.getAll());
    }

    public void create(Context ctx) {
        ReservationRequestDTO request = ctx.bodyAsClass(ReservationRequestDTO.class);
        ctx.status(HttpStatus.CREATED).json(reservationService.insert(request));
    }

    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ReservationRequestDTO request = ctx.bodyAsClass(ReservationRequestDTO.class);
        ctx.json(reservationService.update(id, request));
    }

    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (reservationService.delete(id)) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.status(HttpStatus.NOT_FOUND).result("Reservation not found");
        }
    }
}