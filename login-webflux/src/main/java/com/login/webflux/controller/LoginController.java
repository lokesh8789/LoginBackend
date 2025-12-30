package com.login.webflux.controller;

import com.login.webflux.dto.JwtAuthResponse;
import com.login.webflux.dto.LoginDto;
import com.login.webflux.dto.UserDto;
import com.login.webflux.security.JwtTokenUtil;
import com.login.webflux.security.JwtUser;
import com.login.webflux.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

import java.text.SimpleDateFormat;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final ReactiveUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public Mono<JwtAuthResponse> login(@Valid @RequestBody LoginDto loginDto) {
        log.info("Received login request: {}", loginDto);
        return userDetailsService.findByUsername(loginDto.getUserName())
                .publishOn(Schedulers.boundedElastic())
                .filter(ud -> passwordEncoder.matches(loginDto.getPassword(), ud.getPassword()))
                .cast(JwtUser.class)
                .map(JwtUser::user)
                .flatMap(user -> Mono.fromSupplier(() -> jwtTokenUtil.generateToken(user.getEmail()))
                        .map(token -> Tuples.of(user, token)))
                .map(tuple -> JwtAuthResponse.builder()
                        .token(tuple.getT2())
                        .user(modelMapper.map(tuple.getT1(), UserDto.class))
                        .expiration(new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(jwtTokenUtil.extractExpiration(tuple.getT2())))
                        .build())
                .switchIfEmpty(Mono.error(new BadCredentialsException("Username or password not correct")));
    }
}
