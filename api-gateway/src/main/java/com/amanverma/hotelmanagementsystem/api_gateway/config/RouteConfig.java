package com.amanverma.hotelmanagementsystem.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://AUTH-SERVICE"))
                .route("user-service", r -> r
                        .path("/api/v1/users/**", "/api/v1/admin/users/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://USER-SERVICE"))
                .route("hotel-service", r -> r
                        .path("/api/v1/hotel/**", "/api/v1/hotel-manager/hotel/**", "/api/v1/admin/hotel/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://HOTEL-SERVICE"))
                .build();
    }
}


