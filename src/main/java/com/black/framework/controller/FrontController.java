package com.black.framework.controller;

import java.io.IOException;
import java.util.Map;

import com.black.framework.routing.Route;
import com.black.framework.context.ApplicationContext;
import com.black.framework.enums.requests.RequestMethod;
import com.black.framework.exceptions.RouteNotFoundException;
import com.black.framework.models.RedirectResponse;
import com.black.framework.models.RequestData;
import com.black.framework.models.View;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet{
    private ApplicationContext applicationContext;

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();

        this.applicationContext = (ApplicationContext) ctx.getAttribute("applicationContext");
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

        String path = buildPath(request);

        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        Route route = new Route(requestMethod, path);

        try {
            Object returnVal = applicationContext.invokeHandler(route, new RequestData(request));
            handleResult(returnVal, request, response);
        } catch (RouteNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "No route configured for " + path);
        }        
    }

    private void handleResult(Object returnVal, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
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
            response.sendRedirect(request.getContextPath() + redirectResponse.getToPath());
            return;
        }

        response.getWriter().println("request result: " + returnVal);
    }

    private String generateViewPath(String viewName){
        return applicationContext.getViewPath()+viewName;
    }

    private String buildPath(HttpServletRequest request){
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if(path.isBlank()){
            path = "/";
        }

        return path;
    }
}
