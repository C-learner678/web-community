package com.jlu.webcommunity.core.ehandler;

public class UserBannedException extends RuntimeException{
    public UserBannedException(){
        super();
    }

    public UserBannedException(String message){
        super(message);
    }
}
