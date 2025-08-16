package com.example.wewha.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex) {
        try {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            var body = Map.of(
                    "status", 403,
                    "error", "Forbidden",
                    "message", ex.getMessage(),
                    "path", req.getRequestURI()
            );
            res.getWriter().write(om.writeValueAsString(body));
        } catch (Exception ignored) {}
    }
}
