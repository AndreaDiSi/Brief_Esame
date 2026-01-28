package com.andreadisimone.exceptions.not_found;

public class AccomodationNotFoundException extends RuntimeException {
    
    //costruttori
    public AccomodationNotFoundException(String message){
        super(message);
    }

    public AccomodationNotFoundException(String field, String value){
        super("Accomodation with: " + field + " : " + value + " Not Found");
    }

    public AccomodationNotFoundException(Integer id){
        super("Accomodation with id: " + id + " Not Found");
    }

    //super() è il metodo che chiama il costruttore di RuntimeException, 
    // il messaggio che ci metto dentro è quello che verrà messo dentro il getMessage()
}
