package com.amanverma.hotelmanagementsystem.api_gateway.config;

import com.amanverma.hotelmanagementsystem.api_gateway.filter.JwtAuthenticationFilter;
import com.amanverma.hotelmanagementsystem.api_gateway.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static com.amanverma.hotelmanagementsystem.api_gateway.model.Role.*;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v1/auth/**")
                            .permitAll()
                        .pathMatchers("/api/v1/hotels/**")
                        .permitAll()
                        .pathMatchers("/api/v1/inventory/**")
                        .permitAll()
                        .pathMatchers("/api/v1/users/**")
                            .hasAnyRole(USER.toString(), HOTEL_MANAGER.toString(), ADMIN.toString())
                        .pathMatchers("/api/v1/hotel-manager/**")
                            .hasAnyRole(HOTEL_MANAGER.toString(), ADMIN.toString())
                        .pathMatchers("/api/v1/admin/**")
                            .hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}