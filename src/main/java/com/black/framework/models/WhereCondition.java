package com.black.framework.models;

public class WhereCondition {
    private final String column;
    private final Object value;
    private final String op;
    
    public WhereCondition(String column, Object value){
        this(column, value, "=");
    }

    public WhereCondition(String column, Object value, String op){
        this.column = column;
        this.value = value;
        this.op = op;
    }

    public String buildCondition(){
        return column + " " + op + " ?";
    }

    public Object getValue(){
        return this.value;
    }
}
