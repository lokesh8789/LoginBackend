package com.login.webflux.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtTokenUtil jwtTokenUtil;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        log.info("Processing Request Url: {}", exchange.getRequest().getPath());
        AtomicReference<String> authToken = new AtomicReference<>();
        return Mono.defer(() -> Mono.justOrEmpty(jwtTokenUtil.getJwtToken(exchange.getRequest())))
                .map(token -> authToken.updateAndGet(s -> token))
                .map(jwtTokenUtil::extractUsername)
                .flatMap(userDetailsService::findByUsername)
                .filter(ud -> jwtTokenUtil.validateToken(authToken.get(), ud.getUsername()))
                .map(ud -> UsernamePasswordAuthenticationToken.authenticated(ud, null, ud.getAuthorities()))
                .<SecurityContext>map(SecurityContextImpl::new)
                .onErrorResume(e -> Mono.empty());
    }

}
