package org.springbus.controller;

import org.springbus.mapper.UrlMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/tt")
    @UrlMapping("/tt")
    public  Integer doIt(){
        return  123;
    }
}
