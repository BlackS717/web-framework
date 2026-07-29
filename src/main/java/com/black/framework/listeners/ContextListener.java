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
        
        String driverClassName = "";
        String url = "";
        String username = "";
        String password = "";

        boolean hasDataSourceConfig;

        try {
            Properties properties = ReflectionUtil.instance().loadProperties();
            hasDataSourceConfig = hasDataSourceConfig(properties);

            if (hasDataSourceConfig) {
                driverClassName = getRequiredProperty(
                    properties,
                    "datasource.driver-class-name"
                );

                url = getRequiredProperty(
                    properties,
                    "datasource.url"
                );

                username = getRequiredProperty(
                    properties,
                    "datasource.username"
                );

                password = getRequiredProperty(
                    properties,
                    "datasource.password"
                );
    
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                
                Class.forName(driverClassName, true, classLoader);
            }

            packageName = getRequiredProperty(properties, "controller.package");

            viewPath = getOptionalProperty(properties, "view.path", DEFAULT_VIEW_PATH);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        DataSourceConfig dataSourceConfig = new DataSourceConfig(url, driverClassName, username, password, hasDataSourceConfig);
        
        try {
            ReflectionUtil.instance().generateRoute(packageName, Controller.class, RequestMapping.class, mapping);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize routes",e);
        }
        
        try {

            ApplicationContext applicationContext = new ApplicationContext(mapping, viewPath, dataSourceConfig);
            context.setAttribute("applicationContext", applicationContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getProperty(Properties properties, String propertyName, boolean required){

        String property = properties.getProperty(propertyName);
        if(required && property == null){
            throw new MissingPropertyException(propertyName);
        }

        return property;
    }

    private String getProperty(Properties properties, String propertyName, String defaultValue, boolean required){

        String property = properties.getProperty(propertyName, defaultValue);
        if(required && property == null){
            throw new MissingPropertyException(propertyName);
        }

        return property;
    }

    private String getOptionalProperty(Properties properties, String propertyName){
       return getOptionalProperty(properties, propertyName, null);
    }

    private String getOptionalProperty(Properties properties, String propertyName, String defaultValue){
       return getProperty(properties, propertyName, defaultValue, false);
    }

    private String getRequiredProperty(Properties properties, String propertyName){
        return getProperty(properties, propertyName, true);
    }

    private boolean hasDataSourceConfig(Properties properties){
        return properties.containsKey("datasource.url")
                || properties.containsKey("datasource.driver-class-name")
                || properties.containsKey("datasource.username")
                || properties.containsKey("datasource.password");
    }

}
