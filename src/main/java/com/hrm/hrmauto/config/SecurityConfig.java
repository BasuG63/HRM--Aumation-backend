package com.hrm.hrmauto.config;

import com.hrm.hrmauto.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public SecurityConfig(
            JwtAuthenticationFilter jwtFilter) {

        this.jwtFilter = jwtFilter;
    }

    // =====================================================
    // CORS CONFIGURATION
    // =====================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // React Vite frontend
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "http://localhost:5175"
                )
        );

        // HTTP methods
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );

        // Request headers
        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept",
                        "Origin"
                )
        );

        // Response headers
        configuration.setExposedHeaders(
                List.of(
                        "Authorization",
                        "Content-Disposition"
                )
        );

        // Credentials
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // =====================================================
    // AUTHENTICATION MANAGER
    // =====================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration
                .getAuthenticationManager();
    }

    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            // CORS
            .cors(cors ->
                    cors.configurationSource(
                            corsConfigurationSource()
                    )
            )

            // CSRF
            .csrf(csrf ->
                    csrf.disable()
            )

            // Authorization
            .authorizeHttpRequests(auth -> auth

                    // CORS preflight
                    .requestMatchers(
                            HttpMethod.OPTIONS,
                            "/**"
                    )
                    .permitAll()

                    // Login
                    .requestMatchers(
                            "/api/auth/**"
                    )
                    .permitAll()

                    // HR APIs
                    .requestMatchers(
                            "/api/hr/**"
                    )
                    .hasRole("HR")

                    // Employee APIs
                    .requestMatchers(
                            "/api/employee/**"
                    )
                    .hasAnyRole(
                            "HR",
                            "EMPLOYEE"
                    )

                    // Everything else
                    .anyRequest()
                    .authenticated()
            )

            // Stateless JWT session
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            // JWT filter
            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}