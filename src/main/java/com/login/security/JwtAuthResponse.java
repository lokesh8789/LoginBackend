package com.login.security;

import com.login.dto.UserDto;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
    private UserDto user;
    private String token;
    private String expiration;
}
