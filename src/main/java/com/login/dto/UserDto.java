package com.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(onMethod_ = @JsonIgnore)
    private Integer id;
    @NotBlank
    @Size(min = 2,message = "Name must have more than 2 letters")
    private String name;
    @Email(message = "Email format not valid")
    @NotEmpty
    private String email;
    @Pattern(regexp = "^\\d{10}$",message = "Must Contain 10 Digits")
    private String mobile;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Getter(onMethod_ = @JsonIgnore)
    @NotBlank
    @Size(min = 4,message = "Password must have more than 4 letters")
    private String password;
}
