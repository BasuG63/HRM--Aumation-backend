package com.hrm.hrmauto.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // =====================================================
        // ALLOWED ORIGINS
        // =====================================================

        configuration.setAllowedOrigins(
                List.of(
                        // Local Vite
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://localhost:5175",

                        // Vercel Production
                        "https://hrmauto.vercel.app",

                        // Vercel Preview deployment
                        "https://hrm-2-qut7cgi-tescom1.vercel.app"
                )
        );

        // =====================================================
        // ALLOWED METHODS
        // =====================================================

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS",
                        "HEAD"
                )
        );

        // =====================================================
        // ALLOWED HEADERS
        // =====================================================

        configuration.setAllowedHeaders(
                List.of("*")
        );

        // =====================================================
        // EXPOSED HEADERS
        // =====================================================

        configuration.setExposedHeaders(
                List.of(
                        "Authorization",
                        "Content-Disposition"
                )
        );

        // =====================================================
        // CREDENTIALS
        // =====================================================

        configuration.setAllowCredentials(true);

        // =====================================================
        // PREFLIGHT CACHE
        // =====================================================

        configuration.setMaxAge(3600L);

        // =====================================================
        // REGISTER
        // =====================================================

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}
