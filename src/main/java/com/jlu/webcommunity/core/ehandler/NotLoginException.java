package com.jlu.webcommunity.core.ehandler;

public class NotLoginException extends RuntimeException{
    public NotLoginException(){
        super();
    }

    public NotLoginException(String message){
        super(message);
    }
}
