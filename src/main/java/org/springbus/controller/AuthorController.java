package org.springbus.controller;

import org.springbus.convert.FormObj;
import org.springbus.model.Dept;
import org.springbus.model.Employee;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthorController {

    @RequestMapping(value = "/a" )
    public String test1(@FormObj Dept dept, @FormObj Employee emp, HttpServletRequest request,HttpServletResponse response) {

           String authorization=     request.getHeader("Authorization");
           if(!StringUtils.isEmpty(authorization)){
               authorization=authorization.replace("Basic","");
               authorization=authorization.trim();
               authorization=new String( Base64Utils.decodeFromString(authorization));
               String[] userAndPass=authorization.split(":");
               String user= userAndPass[0];
               String pass= userAndPass[1];
               if(user.equals("roote")) {
                   return user +"---"+ pass;
               }

           }


        return doAuthResult(response);
    }

    private String  doAuthResult(HttpServletResponse response){
        response.setHeader("WWW-Authenticate", "Basic realm=Secure Area");
        response.setStatus(401);
        return "auth";
    }
}
