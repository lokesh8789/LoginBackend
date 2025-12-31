package com.login.webflux.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
//@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtTokenUtil jwtTokenUtil;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("Authenticating user {}", authentication.getName());
        return userDetailsService.findByUsername(authentication.getName())
                .filter(ud -> jwtTokenUtil.validateToken((String) authentication.getCredentials(), ud.getUsername()))
                .<Authentication>map(ud -> UsernamePasswordAuthenticationToken.authenticated(ud, null, ud.getAuthorities()))
                .onErrorResume(e -> Mono.empty())
                .switchIfEmpty(Mono.error(new BadCredentialsException("Access Denied. Unauthorized")));
    }
}
