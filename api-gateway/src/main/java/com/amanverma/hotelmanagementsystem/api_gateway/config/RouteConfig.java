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
                        .path("/api/v1/hotels/**", "/api/v1/hotel-manager/hotels/**", "/api/v1/admin/hotels/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://HOTEL-SERVICE"))
                .route("loyalty-service", r -> r
                        .path("/api/v1/loyalty/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://LOYALTY-SERVICE"))
                .route("inventory-service", r -> r
                        .path("/api/v1/inventory/**", "/api/v1/hotel-manager/inventory/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://INVENTORY-SERVICE"))
                .route("payment-service", r -> r
                        .path("/api/v1/payments/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://PAYMENT-SERVICE"))
                .route("booking-service", r -> r
                        .path("/api/v1/bookings/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://BOOKING-SERVICE"))
                .build();
    }
}


