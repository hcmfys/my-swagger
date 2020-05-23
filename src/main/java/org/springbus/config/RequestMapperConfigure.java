package org.springbus.config;

import org.springbus.convert.DefaultReturnValueResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class RequestMapperConfigure implements WebMvcConfigurer {

    @Autowired(required = false)
    private  List<WebMvcConfigurer> configurers;
    @Autowired(required = false)
    public void setConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {

        }
    }
    @Bean
    public DefaultReturnValueResolver JsonReturnHandler(){
        DefaultReturnValueResolver formatJsonReturnValueHandler=new DefaultReturnValueResolver();
        return formatJsonReturnValueHandler;
    }
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(JsonReturnHandler());
         System.out.println("===========> ======= " +configurers +"  ---->  "+configurers.size());
    }
}
