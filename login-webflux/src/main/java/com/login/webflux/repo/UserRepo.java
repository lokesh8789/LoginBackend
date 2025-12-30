package com.login.webflux.repo;

import com.login.webflux.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepo extends R2dbcRepository<User, Integer> {
    Mono<User> findByEmail(String email);

    Flux<User> findByNameContaining(String name);

    @Query("select id from users where email = :email and password = :password")
    Mono<Integer> findIdByEmailAndPassword(String email, String password);
}
