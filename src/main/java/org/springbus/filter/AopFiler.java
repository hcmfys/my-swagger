package org.springbus.filter;

import org.jooq.meta.derby.sys.Sys;

import javax.servlet.*;
import java.io.IOException;

public class AopFiler implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("start filter");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("end filter");
    }
}
