package com.black.app.controllers;

import com.black.framework.annotation.Controller;
import com.black.framework.annotation.RequestMapping;
import com.black.framework.enums.requests.RequestMethod;

@Controller
public class AnnotatedController {

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello";
    }

    @RequestMapping(path = "/hello", method = RequestMethod.POST)
    public String postHello() {
        return "post hello";
    }
}
