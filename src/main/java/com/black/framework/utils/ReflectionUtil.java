package com.black.framework.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.black.framework.annotation.RequestMapping;
import com.black.framework.routing.Handler;
import com.black.framework.routing.Route;

public class ReflectionUtil {

    private static ReflectionUtil instance;

    private String classExtension = ".class";

    public static ReflectionUtil instance(){
        if(instance == null){
            instance = new ReflectionUtil();
        }

        return instance;
    }

    public void generateRoute(String packageName, Class<? extends Annotation> classAnnotation, Class<? extends Annotation> methodAnnotation, HashMap<Route, Handler> mapping)
        throws IOException, ClassNotFoundException    
    {
        List<Class<?>> classes = getClasses(packageName);

        for(Class<?> clazz: classes){
            if(!clazz.isAnnotationPresent(classAnnotation)){
                // skip class with no annotation
                continue;
            }

            List<Method> annotatedMethods = getAnnotatedMethods(clazz, methodAnnotation);

            if(annotatedMethods.isEmpty()){
                throw new IOException("No annotated method in " + clazz.getName());
            }

            for(Method method: annotatedMethods){
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                Route route = new Route(requestMapping.method(), requestMapping.path());
                Handler handler = null;
                
                if(mapping.containsKey(route)){
                    throw new IllegalStateException("Duplicate route for " + requestMapping.path());
                }
                
                try{
                    Object controllerInstance = clazz.getDeclaredConstructor().newInstance();
                    handler = new Handler(controllerInstance, method);   

                    mapping.put(route, handler);

                } catch(Exception e){
                    throw new IOException("Failed to instantiate the handler");
                }
            }

        }

    }

    private List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException{
        List<Class<?>> classes = new ArrayList<>();
        String classPath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(classPath);

        while(resources.hasMoreElements()){
            URL resource = resources.nextElement();

            File directory = new File(resource.getFile());

            if(directory.exists()){
                File[] files = directory.listFiles();

                if(files == null){
                    // skip empty directory
                    continue;
                }

                for(File file: files){
                    if(!haveExtension(file, classExtension)){
                        // ignore non class file
                        continue;
                    }

                    String name = packageName + "." + getName(file);
                    Class<?> clazz = Class.forName(name);
                    classes.add(clazz);
                }
            }

        }


        return classes;
    }

    public List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotationClass){
        List<Method> annotatedMethods = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();

        for(Method m: methods){
            if(m.isAnnotationPresent(annotationClass)){
               annotatedMethods.add(m);
            }
        }

        return annotatedMethods;
    }

    public Properties loadProperties() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream input = classLoader.getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new FileNotFoundException(
                    "application.properties not found in the classpath"
                );
            }

            Properties properties = new Properties();
            properties.load(input);

            return properties;
        }
    }

    private String getName(File file){
        String name = file.getName();
        return name.substring(0, name.length() - classExtension.length());
    }

    private boolean haveExtension(File file, String extension){
        return  file.getName().endsWith(extension);
    }

}
