package com.andreadisimone.dtos.feedback_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class FeedbackResponseDTO {
    private Integer idFeed;
    private String title;
    private String textFeedback;
    private Integer points;
    private Integer idReservation;
    
}