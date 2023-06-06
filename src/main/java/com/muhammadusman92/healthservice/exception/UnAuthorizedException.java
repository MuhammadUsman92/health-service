package com.muhammadusman92.healthservice.exception;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException() {
        super("You are not authorized for this service");
    }
}
