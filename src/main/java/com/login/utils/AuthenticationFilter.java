package com.login.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

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
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Access-Control-Allow-Origin","*");
            response.setHeader("Access-Control-Allow-Methods","*");
            response.setHeader("Access-Control-Allow-Headers","*");
            return;
        }
        if(request.getRequestURI().equals("/api/login")||request.getRequestURI().equals("/users/register")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        Enumeration<String> headerNames = request.getHeaderNames();
        headerNames.asIterator().forEachRemaining(header -> {
            System.out.println("Header Name:" + header + "   " + "Header Value:" + request.getHeader(header));
        });
        String header = request.getHeader(Constants.AUTHORIZATION);
        if (header==null||header.equals("null")||header.length()==0) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"User Not Authenticated");
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
