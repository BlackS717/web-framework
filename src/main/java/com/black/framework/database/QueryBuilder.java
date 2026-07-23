package com.black.framework.database;

import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {
    private final String table;
    private String select = "SELECT *";
    private String where = " WHERE 1 = 1 ";

    private final Map<String, Object> parameters = new HashMap<>();

    public QueryBuilder(String table){
        this.table = table;
    }

    public QueryBuilder select(String select){
        this.select = select;
        return this;
    }

    public QueryBuilder where(Map<String, Object> whereCondition){
        for(Map.Entry<String,Object> entry: whereCondition.entrySet()){
            this.where += " AND " + entry.getKey() + " = ?";
            this.parameters.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public String getQuery(){
        return select + " FROM " + table + where;
    }

    public Map<String, Object> getParameters(){
        return new HashMap<>(parameters);
    }

}
