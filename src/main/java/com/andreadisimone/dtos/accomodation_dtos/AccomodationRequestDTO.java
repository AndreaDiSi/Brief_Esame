package com.andreadisimone.dtos.accomodation_dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AccomodationRequestDTO {

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
    private int hostId;
}
