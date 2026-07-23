package com.black.framework.models;

import java.util.HashMap;
import java.util.Map;

public class RequestData {
    private final Map<String, String[]> data;

    public RequestData(Map<String, String[]> data){
        this.data = new HashMap<>();

        for (Map.Entry<String, String[]> entry : data.entrySet()) {
            this.data.put(entry.getKey(), entry.getValue().clone());
        }
    }

    public String getParameter(String paramName) {
        String[] values = data.get(paramName);

        if (values == null || values.length == 0) {
            return null;
        }

        return values[0];
    }

    public String[] getParameterValues(String paramName) {
        String[] values = data.get(paramName);
        
        if(values == null){
            return null;
        }

        return values.clone();
    }

    public Map<String, String[]> getDataSet(){
        return this.data;
    }

}
