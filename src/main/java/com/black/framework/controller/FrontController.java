package com.black.framework.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.black.framework.routing.Route;
import com.black.framework.enums.requests.RequestMethod;
import com.black.framework.models.RedirectResponse;
import com.black.framework.models.RequestData;
import com.black.framework.models.View;
import com.black.framework.routing.Handler;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet{
    private Map<Route, Handler> routeMapping = new HashMap<>();
    private String viewPath = "";

    @SuppressWarnings("unchecked")
    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        this.routeMapping =(Map<Route, Handler>) ctx.getAttribute("mapping");
        this.viewPath = String.valueOf(ctx.getAttribute("viewPath"));

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

        Map<String, String[]> requestData = new HashMap<>();

        Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            String paramName = paramNames.nextElement();
            String[] paramValue = request.getParameterValues(paramName);

            requestData.put(paramName, paramValue);
        }

        response.setContentType("text/html;charset=UTF-8");
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if(path.isBlank()){
            path = "/";
        }

        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        Route route = new Route(requestMethod, path);

        if (!routeMapping.containsKey(route)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No route configured for " + path);
            return;
        }

        // if found try to call the method linked to the route

        Handler handler = routeMapping.get(route);

        Object returnVal = handler.invoke(new RequestData(requestData));

        if(returnVal == null){
            response.getWriter().println("request result: null");
            return;
        }

        
        if(returnVal instanceof View view){
            for(Map.Entry<String, Object> entry: view.getData().entrySet()){
                request.setAttribute(entry.getKey(), entry.getValue());
            }

            request.getRequestDispatcher(generateViewPath(view.getToPath())).forward(request, response);
            return;

        }

        if(returnVal instanceof RedirectResponse redirectResponse){
            response.sendRedirect(redirectResponse.getToPath());
            return;
        }

        response.getWriter().println("request result: " + returnVal);
    }

    private String generateViewPath(String viewName){
        return viewPath+viewName;
    }
}
