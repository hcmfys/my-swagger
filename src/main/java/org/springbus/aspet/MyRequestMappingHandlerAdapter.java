package org.springbus.aspet;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE+101)
public class MyRequestMappingHandlerAdapter  extends RequestMappingHandlerAdapter {

}
