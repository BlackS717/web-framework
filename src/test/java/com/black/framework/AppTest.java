package com.black.framework;

import java.util.HashMap;

import com.black.framework.annotation.Controller;
import com.black.framework.annotation.RequestMapping;
import com.black.framework.routing.Handler;
import com.black.framework.routing.Route;
import com.black.framework.utils.ReflectionUtil;

import junit.framework.TestCase;

public class AppTest extends TestCase {

    public void testAnnotationsCreateRouteAndHandler() throws Exception {
        HashMap<Route, Handler> mapping = new HashMap<>();

        ReflectionUtil.instance().generateRoute(
            "com.black.framework",
            Controller.class,
            RequestMapping.class,
            mapping
        );

        Route route = new Route(com.black.framework.enums.requests.RequestMethod.GET, "/hello");
        Route routePost = new Route(com.black.framework.enums.requests.RequestMethod.POST, "/hello");

        assertTrue("Expected route to be generated from annotations", mapping.containsKey(route));

        Handler handler = mapping.get(route);
        assertNotNull("Expected handler to be created", handler);
        assertEquals("hello", handler.invoke());

        handler = mapping.get(routePost);
        assertNotNull("Expected handler to be created", handler);
        assertEquals("post hello", handler.invoke());
    }

    public void testControllersInSubpackagesAreDiscovered() throws Exception {
        HashMap<Route, Handler> mapping = new HashMap<>();

        ReflectionUtil.instance().generateRoute(
            "com.black.framework",
            Controller.class,
            RequestMapping.class,
            mapping
        );

        Route route = new Route(com.black.framework.enums.requests.RequestMethod.GET, "/hello");
        assertTrue("Expected controller in a subpackage to be discovered", mapping.containsKey(route));
    }
}
