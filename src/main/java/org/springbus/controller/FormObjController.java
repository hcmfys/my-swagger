package org.springbus.controller;

import javafx.geometry.Pos;
import org.springbus.convert.FormObj;
import org.springbus.model.Dept;
import org.springbus.model.Employee;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class FormObjController {

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public String test1(@FormObj Dept dept, @FormObj Employee emp) {
        return emp.toString() + " -->" + dept.toString();
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    public String test2(@FormObj("d") Dept dept, @FormObj("e") Employee emp) {
        return emp.toString() + " -->" + dept.toString();
    }

    @RequestMapping(value = "/test3", method = RequestMethod.GET)
    public String test3(@FormObj(value = "d", show = false) Dept dept, @FormObj("e") Employee emp) {
        return emp.toString() + " -->" + dept.toString();
    }

}
