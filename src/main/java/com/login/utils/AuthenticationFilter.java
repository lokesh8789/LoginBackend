package com.login.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthenticationFilter implements Filter {
    @Autowired
    TokenUtil tokenUtil;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("inside filter");
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        log.info(request.getRequestURI());
        if(request.getRequestURI().equals("/api/login")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        String header = request.getHeader(Constants.AUTHORIZATION);
        if (header == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"User is Not Authenticated");
            return;
        }
        boolean validateToken = tokenUtil.validateToken(header);
        if (validateToken){
            filterChain.doFilter(servletRequest,servletResponse);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"User is Not Authenticated");
        }
    }
}
