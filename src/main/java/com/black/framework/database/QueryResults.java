package com.black.framework.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.black.framework.exceptions.ColumnNotFoundException;

public class QueryResults {
    private final List<QueryRow> rows = new ArrayList<>();
    private final List<String> columns;

    public QueryResults(List<String> columns) {
        this.columns = new ArrayList<>(columns);
    }

    public void addRow(List<Object> values){
        rows.add(new QueryRow(this, values));
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnIndex(String name){
        int columnIndex = columns.indexOf(name);

        if(columnIndex < 0){
            throw new ColumnNotFoundException(name);
        }

        return columnIndex;
    }

    public List<QueryRow> getRows(){
        return Collections.unmodifiableList(this.rows);
    }
    
    public List<String> getColumns(){
        return Collections.unmodifiableList(this.columns);
    }
}