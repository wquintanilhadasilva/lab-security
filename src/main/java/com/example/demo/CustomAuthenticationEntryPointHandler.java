package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Defina a resposta de erro personalizada
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token inválido ou ausente");
    }

}
