package com.example.wewha.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) {
        try {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            var body = Map.of(
                    "status", 401,
                    "error", "Unauthorized",
                    "message", ex.getMessage(),
                    "path", req.getRequestURI()
            );
            res.getWriter().write(om.writeValueAsString(body));
        } catch (Exception ignored) {}
    }
}