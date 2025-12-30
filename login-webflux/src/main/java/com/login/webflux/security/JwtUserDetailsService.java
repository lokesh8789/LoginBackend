package com.login.webflux.security;

import com.login.webflux.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtUserDetailsService implements ReactiveUserDetailsService {
    private final UserRepo userRepo;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepo.findByEmail(username)
                .map(JwtUser::new)
                .ofType(UserDetails.class)
                .onErrorResume(t -> Mono.empty())
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with email: " + username)));
    }
}
