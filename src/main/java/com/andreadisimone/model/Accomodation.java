package com.andreadisimone.model;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Accomodation {
    private Integer idAccomodation;
    private String name;
    private Integer nRooms;
    private String accomodationAddress;
    private Host Host;
    private Integer nBedPlaces;
    private Integer floor;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer price;

    public Accomodation(String name, Integer nRooms,String accomodationAddress, Host Host, Integer nBedPlaces, Integer floor, LocalDate startDate, LocalDate endDate, Integer price) {
        this.Host = Host;
        this.accomodationAddress = accomodationAddress;
        this.endDate = endDate;
        this.floor = floor;
        this.nBedPlaces = nBedPlaces;
        this.nRooms = nRooms;
        this.name = name;
        this.price = price;
        this.startDate = startDate;
    }


}


