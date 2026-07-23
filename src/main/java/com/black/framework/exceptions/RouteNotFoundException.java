package com.black.framework.exceptions;

import com.black.framework.routing.Route;

public class RouteNotFoundException extends RuntimeException{
    public RouteNotFoundException(Route route){
        super("No route configured for "+ route);
    }
    
}
