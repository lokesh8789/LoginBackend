package com.login.service;

import com.login.dto.LoginDto;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    String validateLogin(LoginDto loginDto);
    Boolean logout(String token);
}
