package com.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

//import javax.security.sasl.AuthenticationException;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
// 인증되지 않은 사용자가 리소스 요청하면 차단하는 클래스
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
   @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authenticationException)
        throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

}
