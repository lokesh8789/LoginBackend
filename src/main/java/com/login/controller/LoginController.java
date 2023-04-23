package com.login.controller;

import com.login.dto.LoginDto;
import com.login.service.LoginService;
import com.login.utils.Constants;
import com.login.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    LoginService loginService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        String token = loginService.validateLogin(loginDto);
        if(token==null){
            return new ResponseEntity<>("Please Enter Correct UserName or Password",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(token,HttpStatus.OK);
    }
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String header = request.getHeader(Constants.AUTHORIZATION);
        if (header == null) {
            return new ResponseEntity<>("User is not Authorized",HttpStatus.UNAUTHORIZED);
        }
        Boolean success = loginService.logout(header);
        if(success){
            return new ResponseEntity<>("Logout Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("User is not Authorized",HttpStatus.UNAUTHORIZED);
    }
}
