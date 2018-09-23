package com.ekarabacak.reactivesecurity.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String msg){
        super(msg);
    }
}
