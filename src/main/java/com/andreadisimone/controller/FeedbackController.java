package com.andreadisimone.controller;

import com.andreadisimone.dtos.feedback_dtos.FeedbackRequestDTO;
import com.andreadisimone.dtos.feedback_dtos.FeedbackResponseDTO;
import com.andreadisimone.service.FeedbackService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController() {
        this.feedbackService = new FeedbackService();
    }

    public void registerRoutes(io.javalin.Javalin app) {
        app.get("/api/v1/feedback", this::getAll);
        app.post("/api/v1/feedback", this::create);
        app.put("/api/v1/feedback/{id}", this::update);
        app.delete("/api/v1/feedback/{id}", this::delete);
    }

    public void getAll(Context ctx) {
        log.info("GET request to fetch all feedbacks");
        ctx.json(feedbackService.getAll());
    }

    public void create(Context ctx) {
        log.info("POST request to create new feedback");
        FeedbackRequestDTO request = ctx.bodyAsClass(FeedbackRequestDTO.class);
        FeedbackResponseDTO response = feedbackService.insert(request);
        ctx.status(HttpStatus.CREATED).json(response);
    }

    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("PUT request to update feedback ID: {}", id);
        FeedbackRequestDTO request = ctx.bodyAsClass(FeedbackRequestDTO.class);
        ctx.json(feedbackService.update(id, request));
    }

    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("DELETE request for feedback ID: {}", id);
        if (feedbackService.delete(id)) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            log.warn("Feedback ID {} not found for deletion", id);
            ctx.status(HttpStatus.NOT_FOUND).result("Feedback not found");
        }
    }
}