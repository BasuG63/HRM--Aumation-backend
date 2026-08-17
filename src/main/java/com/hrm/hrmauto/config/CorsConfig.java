package com.hrm.hrmauto.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
                        "http://localhost:5173",
                        "http://localhost:5175",
                        "https://hrm-2-sooty.vercel.app"
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
                        "OPTIONS"
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
        // REGISTER CONFIGURATION
        // =====================================================

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // =====================================================
    // CORS FILTER
    // =====================================================

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter(
            CorsConfigurationSource source) {

        return new CorsFilter(source);
    }
}
