package com.black.framework.test;

import com.black.framework.annotation.Controller;
import com.black.framework.annotation.RequestMapping;
import com.black.framework.enums.requests.RequestMethod;

@Controller
public class AnnotatedControllerInSubpackage {

    @RequestMapping(path = "/sub", method = RequestMethod.GET)
    public String helloFromSubpackage() {
        return "sub";
    }
}
