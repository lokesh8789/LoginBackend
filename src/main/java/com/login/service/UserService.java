package com.login.service;

import com.login.dto.UserDto;
import com.login.exceptions.UserExistException;

import java.util.List;

public interface UserService {
    public boolean registerUser(UserDto userDto);
    public List<UserDto> getAllUsers();
    public UserDto getUserById(Integer id);
    public UserDto findByEmail(String email);
    public UserDto findByMobile(String mobile);
    public List<UserDto> findByNameContaining(String name);
    public UserDto deleteUser(Integer id);
    public UserDto updateUser(UserDto userDto, Integer id);
    public List<UserDto> getUserUsingPinCode(Integer pinCode);
}
