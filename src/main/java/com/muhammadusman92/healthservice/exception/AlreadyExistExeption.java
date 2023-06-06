package com.muhammadusman92.healthservice.exception;

public class AlreadyExistExeption extends RuntimeException{
    private String fieldName;
    private String fieldValue;
    public AlreadyExistExeption(String fieldName, String fieldValue) {
        super(String.format("%s : %s is already exist in database",fieldName,fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    public AlreadyExistExeption(String fieldName, String fieldValue,String message) {
        super(String.format("%s : %s is already exist in database and %s",fieldName,fieldValue,message));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
