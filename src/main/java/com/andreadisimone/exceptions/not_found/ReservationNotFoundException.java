package com.andreadisimone.exceptions.not_found;

public class ReservationNotFoundException extends RuntimeException {
    
    //costruttori
    public ReservationNotFoundException(String message){
        super(message);
    }

    public ReservationNotFoundException(String field, String value){
        super("Reservation with: " + field + " : " + value + " Not Found");
    }

    public ReservationNotFoundException(Integer id){
        super("Reservation with id: " + id + " Not Found");
    }


}
