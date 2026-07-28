package com.black.framework.context;

import java.util.Map;

import com.black.framework.database.DatabaseManager;
import com.black.framework.exceptions.RouteNotFoundException;
import com.black.framework.models.RequestData;
import com.black.framework.routing.Handler;
import com.black.framework.routing.Route;

public class ApplicationContext {
    private final Map<Route,Handler> routeMapping;
    private final String viewPath;
    private final DatabaseManager databaseManager;

    public ApplicationContext(Map<Route,Handler> routeMapping, String viewPath, DataSourceConfig dataSourceConfig){
        this.routeMapping = Map.copyOf(routeMapping);
        this.viewPath = viewPath;
        this.databaseManager = new DatabaseManager(dataSourceConfig);
    }

    public Object invokeHandler(Route route, RequestData requestData){

        Handler handler = getHandler(route);

        if(handler == null){
            throw new RouteNotFoundException(route);
        }

        return handler.invoke(this, requestData);
    }

    public String getViewPath(){
        return viewPath;
    }
    
    private Handler getHandler(Route route) {
        return routeMapping.get(route);
    }

    public DatabaseManager getDatabaseManager(){
        if(!this.databaseManager.isUseDatabase()){
            return null;
        }

        return this.databaseManager;
    }
}
