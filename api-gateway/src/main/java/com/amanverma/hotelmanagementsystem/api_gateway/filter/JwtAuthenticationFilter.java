package com.amanverma.hotelmanagementsystem.api_gateway.filter;

import com.amanverma.hotelmanagementsystem.api_gateway.exception.ApiException;
import com.amanverma.hotelmanagementsystem.api_gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private final RouteValidator routeValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (!routeValidator.isSecured.test(request)) {
            return chain.filter(exchange);
        }

        String token = null;

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null) {
            HttpCookie jwtCookie = request.getCookies().getFirst("jwt");
            if (jwtCookie != null) {
                token = jwtCookie.getValue();
            }
        }

        if (token == null) {
            return Mono.error(new ApiException("Missing JWT (Authorization header or cookie)", HttpStatus.UNAUTHORIZED));
        }

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String email = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());

            Authentication auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextImpl context = new SecurityContextImpl(auth);

            ServerHttpRequest mutated = request.mutate()
                    .header("X-User-Id", email)
                    .header("X-User-Roles", String.join(",", roles))
                    .build();

            return chain.filter(exchange.mutate().request(mutated).build())
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));

        } catch (Exception e) {
            return Mono.error(new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED));
        }
    }
}