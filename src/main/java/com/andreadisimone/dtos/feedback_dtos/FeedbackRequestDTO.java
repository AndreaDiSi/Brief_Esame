package com.andreadisimone.dtos.feedback_dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class FeedbackRequestDTO {

    @JsonProperty("title")
    private String title;

    @JsonProperty("textFeedback")
    private String textFeedback;

    @JsonProperty("points")
    private Integer points;

    @JsonProperty("idReservation")
    private Integer idReservation;
}