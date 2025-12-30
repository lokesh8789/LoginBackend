package com.login.webflux.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        log.info("Converting Authentication Token");
        return Mono.defer(() -> Mono.justOrEmpty(jwtTokenUtil.getJwtToken(exchange.getRequest())))
                .<Authentication>map(token -> {
                    String username = jwtTokenUtil.extractUsername(token);
                    return UsernamePasswordAuthenticationToken.unauthenticated(username, token);
                }).onErrorResume(e -> Mono.empty());
    }
}
