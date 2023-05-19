package com.login.controller;

import com.login.dto.LoginDto;
import com.login.dto.UserDto;
import com.login.security.JwtAuthResponse;
import com.login.security.JwtTokenHelper;
import com.login.service.ChatGPTService;
import com.login.service.UserService;
import com.login.utils.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
@Slf4j
public class LoginController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private Environment env;
    @Autowired
    private ChatGPTService chatGPTService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        log.info("Login API Triggered");
        Authentication authentication=null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
        } catch (AuthenticationException e){
            log.info("Error in user authentication");
            return new ResponseEntity<>(new ApiResponse("User Not Authorized. Possibly Bad Credentials",false),HttpStatus.UNAUTHORIZED);
        }
        log.info("Authentication Object Created");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        UserDto user = userService.findByEmail(userDetails.getUsername());
        String token = jwtTokenHelper.generateToken(userDetails.getUsername(), user.getId().toString());
        log.info("Token Generated");
        Date expiration = jwtTokenHelper.getExpirationDateFromToken(token);
        return new ResponseEntity<>(new JwtAuthResponse(user,token,new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(expiration)),HttpStatus.OK);
    }
    @GetMapping("/getAIResponse")
    public ResponseEntity<?> getChatGPTResponse(@RequestParam String message){
        log.info("ChatGPT Request API Called For Request: "+message);
        String key = env.getProperty("OPENAI_API_KEY");
        log.info("Key is: "+key);
        String response = chatGPTService.sendRequest(message,key);
        if(response.equalsIgnoreCase("Exception") || response.equalsIgnoreCase("error")
                || response.contains("invalid_request_error")) {
            return new ResponseEntity<>("Exception Occurred!!",HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
