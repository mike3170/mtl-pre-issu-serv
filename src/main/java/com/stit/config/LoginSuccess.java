package com.stit.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginSuccess implements AuthenticationSuccessHandler  {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"user\":\"" + auth.getName() + "\",\"isAuth\":\"valid\"}");
//        resp.getWriter().write("{\"user\":\"" + auth.getName() + "\",\"isAuth\":\"" + auth.isAuthenticated() + "\"}");
    }
    
}
