package com.black.framework.listeners;

import java.util.HashMap;
import java.util.Properties;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.black.framework.annotation.RequestMapping;
import com.black.framework.context.ApplicationContext;
import com.black.framework.context.DataSourceConfig;
import com.black.framework.exceptions.MissingPropertyException;
import com.black.framework.routing.Handler;
import com.black.framework.routing.Route;
import com.black.framework.utils.ReflectionUtil;
import com.black.framework.annotation.Controller;

@WebListener
public class ContextListener implements ServletContextListener{
    private String DEFAULT_VIEW_PATH = "/WEB-INF/views";

    @Override
    public void contextInitialized(ServletContextEvent sc){
        System.out.println(">> FRAMEWORK INITIALIZATION");
        ServletContext context = sc.getServletContext();
        HashMap<Route, Handler> mapping = new HashMap<>();

        String packageName;
        String viewPath;
        String url;
        String username;
        String password;
        String driverClassName;

        try {
            Properties properties = ReflectionUtil.instance().loadProperties();
            driverClassName = properties.getProperty("datasource.driver-class-name", "");
            url = properties.getProperty("datasource.url", "");
            username = properties.getProperty("datasource.username", "");
            password = properties.getProperty("datasource.password", "");

            viewPath = properties.getProperty("view.path", DEFAULT_VIEW_PATH);

            packageName = properties.getProperty("controller.package");
            if(packageName == null){
                throw new MissingPropertyException(packageName);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        DataSourceConfig dataSourceConfig = new DataSourceConfig(url, driverClassName, username, password);
        
        try {
            ReflectionUtil.instance().generateRoute(packageName, Controller.class, RequestMapping.class, mapping);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize routes",e);
        }
        
        ApplicationContext applicationContext = new ApplicationContext(mapping, viewPath, dataSourceConfig);
        context.setAttribute("applicationContext", applicationContext);
    }
    
}
