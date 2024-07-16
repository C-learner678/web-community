package com.jlu.webcommunity.ehandler;

public class NotLoginException extends RuntimeException{
    public NotLoginException(){
        super();
    }

    public NotLoginException(String message){
        super(message);
    }
}
