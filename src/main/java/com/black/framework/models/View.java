package com.black.framework.models;

import java.util.HashMap;
import java.util.Map;

public class View {
    private final String toPath;
    private final Map<String, Object> data;

    public View(String _toPath){
        this(_toPath, new HashMap<>());
    }

    public View(String _toPath, Map<String, Object> data){
        this.toPath = _toPath;
        this.data = new HashMap<>(data);
    }

    public String getToPath(){
        return this.toPath;
    }

    public Map<String, Object> getData(){
        return this.data;
    }
}
