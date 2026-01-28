package com.andreadisimone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Feedback {
    private Integer idFeed;
    private String title;
    private String text;
    private Integer points;
    private Reservation idReservation;
    
}
