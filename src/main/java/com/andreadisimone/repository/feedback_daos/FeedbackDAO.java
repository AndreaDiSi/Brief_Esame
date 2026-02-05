package com.andreadisimone.repository.feedback_daos;

import java.util.List;
import java.util.Optional;

import com.andreadisimone.dtos.feedback_dtos.FeedbackRequestDTO;
import com.andreadisimone.dtos.feedback_dtos.FeedbackResponseDTO;

public interface FeedbackDAO {
    
    // ==================== CREATE ====================
    FeedbackResponseDTO create(FeedbackRequestDTO request);
    
    // ==================== READ ====================
    List<FeedbackResponseDTO> findAll();
    Optional<FeedbackResponseDTO> findById(Integer idFeed);

    // ==================== UPDATE ====================
    FeedbackResponseDTO update(Integer id, FeedbackRequestDTO request);

    // ==================== DELETE ====================
    boolean deleteById(Integer idFeed);
}