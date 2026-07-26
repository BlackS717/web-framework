package com.black.framework.exceptions;

public class InvalidColumnIndexException extends RuntimeException{
    public InvalidColumnIndexException(int index, int max){
        super("Index column index" + index + ". Expected and index between 0 and " + max);
    }
}
