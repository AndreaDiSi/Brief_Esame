package com.andreadisimone.dtos.reservation_dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReservationRequestDTO {
    @JsonProperty("reservationStartDate")
    private LocalDate reservationStartDate;

    @JsonProperty("reservationEndDate")
    private LocalDate reservationEndDate;

    @JsonProperty("idTenant")
    private Integer idTenant;

    @JsonProperty("idAccomodation")
    private Integer idAccomodation;
}