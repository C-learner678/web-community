package com.jlu.webcommunity.ehandler;

public class NotAdminException extends RuntimeException{
    public NotAdminException(){
        super();
    }

    public NotAdminException(String message){
        super(message);
    }
}
