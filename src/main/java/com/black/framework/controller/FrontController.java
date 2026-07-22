package com.black.framework.controller;

import java.io.IOException;
import java.util.HashMap;

import com.black.framework.routing.Route;
import com.black.framework.enums.requests.RequestMethod;
import com.black.framework.routing.Handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet{
    private HashMap<Route, Handler> routeMapping = new HashMap<>();

    @Override
    public void init() throws ServletException {
        this.routeMapping =
            (HashMap<Route, Handler>) getServletContext().getAttribute("mapping");

    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{


        System.out.println(">>> REQUEST RECEIVED: " + request.getRequestURI());

        response.setContentType("text/html;charset=UTF-8");
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if(path.isBlank()){
            path = "/";
        }

        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        Route route = new Route(requestMethod, path);

        if(!routeMapping.containsKey(route)){
            throw new IOException("No route configured for " + path);
        }

        // if found try to call the method linked to the route

        Handler handler = routeMapping.get(route);

        Object returnVal = handler.invoke();

        if(returnVal == null){
            response.getWriter().println("request result: null");
            return;
        }

        response.getWriter().println("request result: " + returnVal);

        //request.getRequestDispatcher(path).forward(request, response);
    }
}
