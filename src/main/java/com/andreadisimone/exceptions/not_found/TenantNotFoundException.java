package com.andreadisimone.exceptions.not_found;

public class TenantNotFoundException extends RuntimeException{
    //costruttori
    public TenantNotFoundException(String message){
        super(message);
    }

    public TenantNotFoundException(String field, String value){
        super("Tenant with: " + field + " : " + value + " Not Found");
    }

    public TenantNotFoundException(Integer id){
        super("Tenant with id: " + id + " Not Found");
    }
}
