package com.black.framework.listeners;

import java.lang.reflect.Method;
import java.util.HashMap;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import com.black.framework.annotation.RequestMapping;
import com.black.framework.routing.Handler;
import com.black.framework.routing.Route;
import com.black.framework.utils.ReflectionUtil;
import com.black.framework.annotation.Controller;


public class ContextListener implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent sc){
        
        ServletContext context = sc.getServletContext();
        String packageName = context.getInitParameter("controller-package");
        HashMap<Route, Handler> mapping = new HashMap<>();
        String viewPrefix = context.getInitParameter("view-path");
        
        try {
            ReflectionUtil.instance().generateRoute(packageName, Controller.class, RequestMapping.class, mapping);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize routes",e);
        }



        context.setAttribute("mapping", mapping);
        context.setAttribute("view-path", viewPrefix);
    }
    
}
