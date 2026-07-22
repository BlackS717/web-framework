package com.black.framework.routing;

import java.lang.reflect.Method;

public class Handler {
    private final Object controllerInstance;
    private final Method controllerMethod;

    public Handler(Object controllerInstane, Method controllerMethod){
        this.controllerInstance = controllerInstane;
        this.controllerMethod = controllerMethod;
    }

    public Object getControllerInstance() {
        return controllerInstance;
    }

    public Method getControllerMethod() {
        return controllerMethod;
    }

    public Object invoke(){
        if(controllerMethod == null){
            return null;
        }
        try {
            return controllerMethod.invoke(controllerInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}
