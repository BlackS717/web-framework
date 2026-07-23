package com.black.framework;

import java.util.Map;

import com.black.framework.annotation.Controller;
import com.black.framework.annotation.RequestMapping;
import com.black.framework.enums.requests.RequestMethod;

@Controller
public class AnnotatedController {

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public String hello(Map<String, String[]> data) {
        return "hello";
    }

    @RequestMapping(path = "/hello", method = RequestMethod.POST)
    public String postHello(Map<String, String[]> data) {
        return "post hello";
    }
}
