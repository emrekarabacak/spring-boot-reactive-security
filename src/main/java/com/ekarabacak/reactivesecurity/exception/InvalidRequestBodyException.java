package com.ekarabacak.reactivesecurity.exception;

public class InvalidRequestBodyException extends RuntimeException {

    public InvalidRequestBodyException(String msg){
        super(msg);
    }
}
