package com.login.service;

import com.login.dto.UserDto;
import com.login.exceptions.UserExistException;

import java.util.List;

public interface UserService {
    boolean registerUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Integer id);
    UserDto findByEmail(String email);
    List<UserDto> findByNameContaining(String name);
    UserDto deleteUser(Integer id);
    UserDto updateUser(UserDto userDto, Integer id);
}
