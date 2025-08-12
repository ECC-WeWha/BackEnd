package com.example.wewha.comments.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {
    private SecurityUtils() {}
    public static Long currentUserIdOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object p = auth.getPrincipal();
        if (p instanceof Long l) return l;
        if (p instanceof String s) { try { return Long.parseLong(s); } catch (NumberFormatException ignore) {} }
        if (p instanceof UserDetails ud) { try { return Long.parseLong(ud.getUsername()); } catch (NumberFormatException ignore) {} }
        return null;
    }
}