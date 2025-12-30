package com.login.webflux.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class JwtAuthResponse {
    private UserDto user;
    private String token;
    private String expiration;
}
