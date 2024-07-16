package com.jlu.webcommunity.ehandler;

public class UserBannedException extends RuntimeException{
    public UserBannedException(){
        super();
    }

    public UserBannedException(String message){
        super(message);
    }
}
