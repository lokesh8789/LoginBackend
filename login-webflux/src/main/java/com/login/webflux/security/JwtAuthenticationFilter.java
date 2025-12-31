package com.login.webflux.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.SignatureException;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
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
                .flatMap(userDetailsService::findByUsername)
                .filter(userDetails -> jwtTokenUtil.validateToken(authToken.get(), userDetails.getUsername()))
                .map(ud -> UsernamePasswordAuthenticationToken.authenticated(ud, null, ud.getAuthorities()))
                .flatMap(auth -> chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
                        .then(Mono.just(true))) // Dummy Signal So that chain.filter does not execute again in switchIfEmpty
                .onErrorResume(ExpiredJwtException.class, e -> {
                    log.error("Authentication Token Expired or not Valid: {}", e.getMessage());
                    return chain.filter(exchange).then(Mono.just(false));
                })
                .onErrorResume(SignatureException.class, e -> {
                    log.error("Authentication Token Signature Error: {}", e.getMessage());
                    return chain.filter(exchange).then(Mono.just(false));
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Authentication Failed.Username or Password not valid: {}", e.getMessage());
                    return chain.filter(exchange).then(Mono.just(false));
                })
                .switchIfEmpty(Mono.defer(() -> chain.filter(exchange).then(Mono.just(false))))
                .then();
    }
}
