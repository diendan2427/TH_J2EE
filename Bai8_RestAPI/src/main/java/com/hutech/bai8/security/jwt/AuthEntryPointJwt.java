package com.hutech.bai8.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {} for path: {}", authException.getMessage(), request.getRequestURI());

        // Check if this is an API request
        String requestUri = request.getRequestURI();
        boolean isApiRequest = requestUri.startsWith("/api/");
        
        // Check if client accepts JSON (for AJAX/API requests)
        String acceptHeader = request.getHeader("Accept");
        boolean acceptsJson = acceptHeader != null && acceptHeader.contains("application/json");
        
        // Check if already on login page or error page to avoid redirect loop
        boolean isLoginOrErrorPage = requestUri.startsWith("/login") || 
                                      requestUri.startsWith("/error") ||
                                      requestUri.equals("/") ||
                                      requestUri.startsWith("/css/") ||
                                      requestUri.startsWith("/js/") ||
                                      requestUri.startsWith("/images/");
        
        if (isApiRequest || acceptsJson) {
            // Return JSON for API requests
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            final Map<String, Object> body = new HashMap<>();
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", "Unauthorized");
            body.put("message", authException.getMessage());
            body.put("path", request.getRequestURI());

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        } else if (isLoginOrErrorPage) {
            // For login/error pages, just return 401 without redirect to avoid loop
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication required");
        } else {
            // Redirect to login page for web requests
            response.sendRedirect("/login");
        }
    }
}
