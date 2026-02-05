package com.andreadisimone.exceptions.not_found;

public class FeedbackNotFoundException extends RuntimeException {
    
    //costruttori
    public FeedbackNotFoundException(String message){
        super(message);
    }

    public FeedbackNotFoundException(String field, String value){
        super("Feedback with: " + field + " : " + value + " Not Found");
    }

    public FeedbackNotFoundException(Integer id){
        super("Feedback with id: " + id + " Not Found");
    }

    //super() è il metodo che chiama il costruttore di RuntimeException, 
    // il messaggio che ci metto dentro è quello che verrà messo dentro il getMessage()
}
