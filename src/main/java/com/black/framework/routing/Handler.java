package com.black.framework.routing;

import java.lang.reflect.Method;

import com.black.framework.context.ApplicationContext;
import com.black.framework.database.DatabaseManager;

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

    public Object invoke(ApplicationContext applicationContext, Object... data){
        return invokeWithArguments(applicationContext, data);
    }

    private Object invokeWithArguments(ApplicationContext applicationContext, Object... args) {
        if (controllerMethod == null) {
            return null;
        }

        try {
            Class<?>[] parameterTypes = controllerMethod.getParameterTypes();
            Object[] resolvedArguments = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];

                if (parameterType == DatabaseManager.class) {
                    resolvedArguments[i] = applicationContext.getDatabaseManager();
                    continue;
                }

                for (Object arg : args) {
                    if (parameterType.isInstance(arg)) {
                        resolvedArguments[i] = arg;
                        break;
                    }
                }

                if (resolvedArguments[i] == null) {
                    throw new Exception(
                        "Unresolved type " + parameterType.getSimpleName()
                    );
                }
            }

            return controllerMethod.invoke(controllerInstance, resolvedArguments);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
