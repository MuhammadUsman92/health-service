package com.muhammadusman92.healthservice.exception;

public class ResourceNotFoundException extends RuntimeException{
    private String name;
    private String fieldName;
    private Integer fieldValue;
    private String fieldValueString;

    public ResourceNotFoundException(String name, String fieldName, Integer fieldValue) {
        super(String.format("%s is not found with %s : %s",name,fieldName,fieldValue));
        this.name = name;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    public ResourceNotFoundException(String name, String fieldName, String fieldValueString) {
        super(String.format("%s is not found with %s : %s",name,fieldName,fieldValueString));
        this.name = name;
        this.fieldName = fieldName;
        this.fieldValueString = fieldValueString;
    }
}
