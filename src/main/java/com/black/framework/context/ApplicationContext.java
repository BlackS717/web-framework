package com.black.framework.context;

import java.util.Map;

import com.black.framework.exceptions.RouteNotFoundException;
import com.black.framework.models.RequestData;
import com.black.framework.routing.Handler;
import com.black.framework.routing.Route;

public class ApplicationContext {
    private final Map<Route,Handler> routeMapping;
    private final String viewPath;

    public ApplicationContext(Map<Route,Handler> routeMapping, String viewPath){
        this.routeMapping = Map.copyOf(routeMapping);
        this.viewPath = viewPath;
    }

    public Object invokeHandler(Route route,  RequestData requestData){
        Handler handler = getHandler(route);

        if(handler == null){
            throw new RouteNotFoundException(route);
        }

        return handler.invoke(requestData);
    }

    
    public String getViewPath(){
        return viewPath;
    }
    
    private Handler getHandler(Route route) {
        return routeMapping.get(route);
    }
}
