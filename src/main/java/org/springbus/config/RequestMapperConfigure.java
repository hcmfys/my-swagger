package org.springbus.config;

import org.springbus.convert.DefaultReturnValueResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class RequestMapperConfigure implements WebMvcConfigurer {

    @Bean
    public DefaultReturnValueResolver JsonReturnHandler(){
        DefaultReturnValueResolver formatJsonReturnValueHandler=new DefaultReturnValueResolver();
        return formatJsonReturnValueHandler;
    }
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(JsonReturnHandler());
    }
}
