package com.black.framework.database;

import java.util.ArrayList;
import java.util.List;

import com.black.framework.exceptions.InvalidColumnIndexException;

public class QueryRow {
    private final QueryResults parent;
    private final List<Object> values;

    QueryRow(QueryResults parent, List<Object> values){
        this.parent = parent;
        this.values = new ArrayList<>(values);
    }

    public Object get(String columnName){
        int index = parent.getColumnIndex(columnName);
        return get(index);
    }

    public Object get(int columnIndex) {
        int columnCount = parent.getColumns().size();

        if (columnIndex < 0 || columnIndex >= columnCount) {
            throw new InvalidColumnIndexException(columnIndex, columnCount - 1);
        }

        return values.get(columnIndex);
    }
}
