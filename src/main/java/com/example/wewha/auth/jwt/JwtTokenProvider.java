package com.example.wewha.auth.jwt;
import io.jsonwebtoken.JwtParser;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyString;  // 원래 문자열

    private Key secretKey;           // 실제 서명에 사용하는 Key

    private JwtParser jwtParser;
    private final CustomUserDetailsService userDetailsService;

    private final long tokenValidityInMilliseconds = 1000L * 60 * 60; // 1시간
    private static final long CLOCK_SKEW_SECONDS = 60; // ✅ 60초 허용

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKeyString.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);


        // ✅ 허용 오차 반영된 파서 빌드
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(CLOCK_SKEW_SECONDS)
                .build();
    }

    // 토큰 생성
    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    // 인증 정보 추출
    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 사용자명 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token); // ✅
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // 만료만 별도 로깅(불필요한 예외 메시지 노이즈 줄이기)
            System.out.println("Expired JWT: exp=" + e.getClaims().getExpiration()
                    + ", now=" + new Date());
            return false;
        }
    }

    // HTTP 요청 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " 이후 토큰
        }
        return null;
    }
}
