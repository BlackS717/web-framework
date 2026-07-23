package com.black.framework;

import com.black.framework.annotation.Controller;
import com.black.framework.annotation.RequestMapping;
import com.black.framework.enums.requests.RequestMethod;
import com.black.framework.models.RequestData;

@Controller
public class AnnotatedController {

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public String hello(RequestData data) {
        return "hello";
    }

    @RequestMapping(path = "/hello", method = RequestMethod.POST)
    public String postHello(RequestData data) {
        return "post hello";
    }
}
