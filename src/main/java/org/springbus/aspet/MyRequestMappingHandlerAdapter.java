package org.springbus.aspet;

import org.springbus.convert.DefaultReturnValueResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

//@Component
//@Order(Ordered.LOWEST_PRECEDENCE)
public class MyRequestMappingHandlerAdapter  extends RequestMappingHandlerAdapter {

    public MyRequestMappingHandlerAdapter() {
        super();

    }


    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
        handlers.add(new DefaultReturnValueResolver());
        setReturnValueHandlers(handlers);
    }

}
