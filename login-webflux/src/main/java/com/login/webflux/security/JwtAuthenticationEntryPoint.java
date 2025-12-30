package com.login.webflux.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.info("[ServerAuthenticationEntryPoint]");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Function<String, Mono<DataBuffer>> responseBodyFunction = body -> Mono.defer(() -> Mono.just(response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))));
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage())))
                .flatMap(body -> exchange.getResponse().writeWith(responseBodyFunction.apply(body)))
                .onErrorResume(e -> {
                    DataBuffer errorBody = response.bufferFactory().wrap(("{\"error\":\"Access Denied !!, Unauthorized\"}").getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(errorBody));
                });
    }
}
