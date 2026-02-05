package com.andreadisimone.dtos.accomodation_dtos;

import java.time.LocalDate;

import com.andreadisimone.dtos.host_dtos.HostResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AccomodationResponseDTO {

    @JsonProperty("idAccomodation")
    private Integer idAccomodation;

    @JsonProperty("accomodationName")
    private String accomodationName;

    @JsonProperty("nRooms")
    private Integer nRooms;

    @JsonProperty("accomodationAddress")
    private String accomodationAddress;

    @JsonProperty("nBedPlaces")
    private Integer nBedPlaces;

    @JsonProperty("floor")
    private Integer floor;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("endDate")
    private LocalDate endDate;

    @JsonProperty("startDate")
    private LocalDate startDate;

    @JsonProperty("hostId")
    private Integer hostId;

    @JsonProperty("nReservations")
    private Integer nReservations;
}
