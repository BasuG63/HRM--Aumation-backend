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

        // -------------------------------------------------
        // FRONTEND ORIGINS
        // -------------------------------------------------

        configuration.setAllowedOrigins(
                List.of(
                        // Local Vite
                        "http://localhost:5173",

                        // Alternative local Vite port
                        "http://localhost:5175",

                        // Vercel production
                        "https://hrm-automation-frontend.vercel.app"
                )
        );


        // -------------------------------------------------
        // HTTP METHODS
        // -------------------------------------------------

        configuration.setAllowedMethods(
                List.of(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.OPTIONS.name()
                )
        );


        // -------------------------------------------------
        // REQUEST HEADERS
        // -------------------------------------------------

        configuration.setAllowedHeaders(
                List.of("*")
        );


        // -------------------------------------------------
        // EXPOSED RESPONSE HEADERS
        // -------------------------------------------------

        configuration.setExposedHeaders(
                List.of(
                        "Authorization",
                        "Content-Disposition"
                )
        );


        // -------------------------------------------------
        // CREDENTIALS
        // -------------------------------------------------

        configuration.setAllowCredentials(true);


        // -------------------------------------------------
        // REGISTER CORS CONFIGURATION
        // -------------------------------------------------

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

            // -------------------------------------------------
            // CORS
            // -------------------------------------------------

            .cors(cors ->
                    cors.configurationSource(
                            corsConfigurationSource()
                    )
            )


            // -------------------------------------------------
            // CSRF
            // -------------------------------------------------

            .csrf(csrf ->
                    csrf.disable()
            )


            // -------------------------------------------------
            // AUTHORIZATION
            // -------------------------------------------------

            .authorizeHttpRequests(auth -> auth

                    // -----------------------------------------
                    // CORS PREFLIGHT
                    // -----------------------------------------

                    .requestMatchers(
                            HttpMethod.OPTIONS,
                            "/**"
                    )
                    .permitAll()


                    // -----------------------------------------
                    // AUTHENTICATION
                    // -----------------------------------------

                    .requestMatchers(
                            "/api/auth/**"
                    )
                    .permitAll()


                    // -----------------------------------------
                    // SWAGGER / OPENAPI
                    // -----------------------------------------

                    .requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**"
                    )
                    .permitAll()


                    // -----------------------------------------
                    // HR APIs
                    // -----------------------------------------

                    .requestMatchers(
                            "/api/hr/**"
                    )
                    .hasRole("HR")


                    // -----------------------------------------
                    // EMPLOYEE APIs
                    // -----------------------------------------

                    .requestMatchers(
                            "/api/employee/**"
                    )
                    .hasAnyRole(
                            "HR",
                            "EMPLOYEE"
                    )


                    // -----------------------------------------
                    // EVERYTHING ELSE
                    // -----------------------------------------

                    .anyRequest()
                    .authenticated()
            )


            // -------------------------------------------------
            // STATELESS JWT SESSION
            // -------------------------------------------------

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )


            // -------------------------------------------------
            // JWT FILTER
            // -------------------------------------------------

            .addFilterBefore(
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
            );


        return http.build();
    }
}
