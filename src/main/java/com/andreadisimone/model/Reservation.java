package com.andreadisimone.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reservation {
    private Integer id_reservation;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;
    private Tenant idTenant;
    private Accomodation idAccomodation;
}
