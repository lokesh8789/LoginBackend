package com.login.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{
//    @Autowired
//    @Qualifier("handlerExceptionResolver")
//    private HandlerExceptionResolver handlerExceptionResolver;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("Sending UnAuthorized Error");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Access Denied !!");
        //handlerExceptionResolver.resolveException(request,response,null,authException);
    }
}
