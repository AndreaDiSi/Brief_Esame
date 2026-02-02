package com.andreadisimone.dtos.accomodation_dtos;

import java.time.LocalDate;

import com.andreadisimone.model.Host;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AccomodationResponseDTO {
    private Integer idAccomodation;
    private String accomodationName;
    private Integer nRooms;
    private String accomodationAddress;
    private Integer nBedPlaces;
    private Integer floor;
    private Integer price;
    private LocalDate endDate;
    private LocalDate startDate;
    private int hostId;
    private Host host;
}
