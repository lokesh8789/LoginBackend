package com.login.webflux.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.SignatureException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        log.info("Processing Request: {}", exchange.getRequest().getPath());

        AtomicReference<String> authToken = new AtomicReference<>();
        return Mono.defer(() -> Mono.justOrEmpty(jwtTokenUtil.getJwtToken(exchange.getRequest())))
                .map(token -> authToken.updateAndGet(s -> token))
                .map(jwtTokenUtil::extractUsername)
                .filterWhen(username -> ReactiveSecurityContextHolder.getContext()
                        .map(securityContext -> Objects.isNull(securityContext.getAuthentication())))
                .flatMap(userDetailsService::findByUsername)
                .filter(userDetails -> jwtTokenUtil.validateToken(authToken.get(), userDetails))
                .map(ud -> UsernamePasswordAuthenticationToken.authenticated(ud, null, ud.getAuthorities()))
                .flatMap(auth -> ReactiveSecurityContextHolder.getContext()
                        .doOnNext(securityContext -> securityContext.setAuthentication(auth))
                        .then())
                .onErrorResume(ExpiredJwtException.class, e -> {
                    log.error("Authentication Token Expired or not Valid: {}", e.getMessage());
                    return chain.filter(exchange);
                })
                .onErrorResume(SignatureException.class, e -> {
                    log.error("Authentication Token Signature Error: {}", e.getMessage());
                    return chain.filter(exchange);
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Authentication Failed.Username or Password not valid: {}", e.getMessage());
                    return chain.filter(exchange);
                }).switchIfEmpty(chain.filter(exchange));
    }
}
