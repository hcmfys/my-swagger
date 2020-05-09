package org.springbus.aspet;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**

 *
 */
@Order(1)
@Aspect
@Component
class ProtocolAspect {


    @Before("target(org.springbus.controller.UserController) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void protocol(JoinPoint point) {
        try {
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            Object[] args = point.getArgs();
            int argsNum = args.length;
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();


            System.out.println("-->"+ method.toString());
   // response.getWriter().write("ok");
            return;


        } catch (Exception e) {

        } finally {

        }
    }





}
