package com.andreadisimone.exceptions.not_found;

public class HostNotFoundException extends RuntimeException {
    
    //costruttori
    public HostNotFoundException(String message){
        super(message);
    }

    public HostNotFoundException(String field, String value){
        super("Host with: " + field + " : " + value + " Not Found");
    }

    public HostNotFoundException(Integer id){
        super("Host with id: " + id + " Not Found");
    }
}
