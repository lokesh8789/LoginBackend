package com.login.webflux.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDto {
    @NotEmpty(message = "Username must not be Empty")
    private String userName;
    @NotEmpty(message = "Password must not be Empty")
    private String password;
}
