package com.black.framework.routing;

import com.black.framework.enums.requests.RequestMethod;

import java.lang.reflect.Method;
import java.util.Objects;

public class Route {
    private final RequestMethod requestMethod;
    private final String path;

    public Route(RequestMethod requestMethod, String path){
        this.requestMethod = requestMethod;
        this.path = path;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Route route = (Route) o;

        return requestMethod == route.requestMethod
                && Objects.equals(path, route.path);
    }

@Override
public int hashCode() {
    return Objects.hash(requestMethod, path);
}

}
