package com.black.framework.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.black.framework.models.WhereCondition;

public class QueryBuilder {
    private final String table;
    private String select = "SELECT *";
    private String where = " WHERE 1 = 1 ";

    private final List<Object> parameters = new ArrayList<>();

    public QueryBuilder(String table){
        this.table = table;
    }

    public QueryBuilder select(String select){
        this.select = select;
        return this;
    }

    public QueryBuilder where(WhereCondition...whereConditions){
        for (WhereCondition whereCondition : whereConditions) {
            this.where += " AND " + whereCondition.buildCondition();
            this.parameters.add(whereCondition.getValue());
        }
        return this;
    }

    public String getQuery(){
        return select + " FROM " + table + where;
    }

    public List<Object> getParameters(){
        return new ArrayList<>(parameters);
    }

}
