package com.andreadisimone.service;

import java.util.List;

import com.andreadisimone.dtos.feedback_dtos.FeedbackRequestDTO;
import com.andreadisimone.dtos.feedback_dtos.FeedbackResponseDTO;
import com.andreadisimone.exceptions.not_found.FeedbackNotFoundException; // Assicurati di crearla
import com.andreadisimone.repository.feedback_daos.FeedbackDAO;
import com.andreadisimone.repository.feedback_daos.FeedbackDAOImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeedbackService {

    private final FeedbackDAO feedbackDAO;

    public FeedbackService() {
        
        this.feedbackDAO = new FeedbackDAOImpl();
    }

    public FeedbackResponseDTO insert(FeedbackRequestDTO request) {
        log.info("Attempting to create a new feedback for Reservation ID: {}", request.getIdReservation());

        validatePoints(request.getPoints());

        try {
            return feedbackDAO.create(request);
        } catch (Exception e) {
            log.error("Error during feedback creation: {}", e.getMessage());
            throw new RuntimeException("Could not complete feedback submission: " + e.getMessage());
        }
    }

    public List<FeedbackResponseDTO> getAll() {
        log.info("Fetching all feedbacks from database");
        return feedbackDAO.findAll();
    }

    public boolean delete(Integer idFeed) {
        log.info("Attempting to delete feedback with ID: {}", idFeed);
        
        boolean deleted = feedbackDAO.deleteById(idFeed);
        if (!deleted) {
            log.warn("Feedback with ID: {} not found for deletion", idFeed);
        }
        return deleted;
    }

    public FeedbackResponseDTO update(Integer idFeed, FeedbackRequestDTO request) {
        log.info("Attempting to update feedback with ID: {}", idFeed);

        validatePoints(request.getPoints());

        try {
            FeedbackResponseDTO updated = feedbackDAO.update(idFeed, request);
            if (updated == null) {
                throw new FeedbackNotFoundException("Feedback with ID " + idFeed + " not found");
            }
            return updated;
        } catch (Exception e) {
            log.error("Update failed for feedback ID {}: {}", idFeed, e.getMessage());
            throw e;
        }
    }

    private void validatePoints(Integer points) {
        if (points == null || points < 1 || points > 5) {
            log.error("Validation failed: points {} out of range (1-5)", points);
            throw new IllegalArgumentException("Feedback points must be between 1 and 5");
        }
    }
}