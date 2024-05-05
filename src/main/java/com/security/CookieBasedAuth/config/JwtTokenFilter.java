package com.security.CookieBasedAuth.config;

import com.security.CookieBasedAuth.services.JwtService;
import com.security.CookieBasedAuth.services.TokenBlackListService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenBlackListService tokenBlackListService;

    @Autowired
    private JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtService.extractTokenFromRequest(request);

        if (token != null && !tokenBlackListService.isBlacklisted(token)) {
            // Token is valid and not blacklisted
            // Proceed with request processing
            filterChain.doFilter(request, response);
        } else {
            // Token is blacklisted or expired, deny access
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

