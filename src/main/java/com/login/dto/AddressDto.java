package com.login.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Integer id;
    @NotEmpty
    private String town;
    @NotEmpty
    private String district;
    @NotEmpty
    private String state;
    @NotEmpty
    private String country;
    @NotNull
    private Integer pinCode;
    @JsonBackReference
    private UserDto userDto;
}
