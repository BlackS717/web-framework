package com.black.framework.models;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

public class RequestData {
    private final Map<String, String[]> data;

    public RequestData(HttpServletRequest request){
        this(extractParameters(request));
    }

    private RequestData(Map<String, String[]> data){
        this.data = new HashMap<>();

        for (Map.Entry<String, String[]> entry : data.entrySet()) {
            this.data.put(entry.getKey(), entry.getValue().clone());
        }
    }

    private static Map<String, String[]> extractParameters(HttpServletRequest request){
        Map<String, String[]> requestData = new HashMap<>();

        Enumeration<String> paramNames = request.getParameterNames();

        while(paramNames.hasMoreElements()){
            String paramName = paramNames.nextElement();
            String[] paramValue = request.getParameterValues(paramName);

            requestData.put(paramName, paramValue);
        }    

        return requestData;
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
