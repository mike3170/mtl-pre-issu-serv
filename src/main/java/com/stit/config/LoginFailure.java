package com.stit.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


public class LoginFailure implements AuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException ex) throws IOException, ServletException {
//        System.out.println("request is authenticate failure.");
        //resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);        
        resp.setContentType("application/json");
        resp.getWriter().write("{\"status\":\"authtnticate failed\",\"user\":\"unknowen\",\"isAuth\":\"invalid\"}");
    }
    
}
