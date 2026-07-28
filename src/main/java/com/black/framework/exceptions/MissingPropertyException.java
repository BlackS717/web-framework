package com.black.framework.exceptions;

public class MissingPropertyException extends RuntimeException{
    public MissingPropertyException(String property) {
        super("application.properties '" + property + "' is missing");
    }
    
}
