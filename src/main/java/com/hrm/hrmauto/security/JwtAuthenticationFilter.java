package com.hrm.hrmauto.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        System.out.println(
                "========================================"
        );

        System.out.println(
                "REQUEST: "
                        + request.getMethod()
                        + " "
                        + request.getRequestURI()
        );

        System.out.println(
                "Authorization Header Present: "
                        + (authHeader != null)
        );

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println(
                    "No valid JWT Authorization header"
            );

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authHeader.substring(7);

        try {

            String email =
                    jwtService.extractUsername(token);

            System.out.println(
                    "JWT Email: " + email
            );

            if (email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(email);

                System.out.println(
                        "User: "
                                + userDetails.getUsername()
                );

                System.out.println(
                        "Authorities: "
                                + userDetails.getAuthorities()
                );

                boolean valid =
                        jwtService.validateToken(
                                token,
                                userDetails
                        );

                System.out.println(
                        "JWT Valid: " + valid
                );

                if (valid) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println(
                            "Authenticated User: "
                                    + authentication.getName()
                    );

                    System.out.println(
                            "Authenticated Authorities: "
                                    + authentication.getAuthorities()
                    );

                    System.out.println(
                            "JWT Authentication SUCCESS"
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT Authentication FAILED: "
                            + e.getMessage()
            );
        }

        filterChain.doFilter(request, response);
    }
}