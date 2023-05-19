package com.login.controller;

import com.login.dto.UserDto;
import com.login.security.JwtTokenHelper;
import com.login.service.SMSService;
import com.login.service.UserService;

import com.login.utils.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    JwtTokenHelper jwtTokenHelper;
    @Autowired
    SMSService smsService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserDto userDto){
        boolean success = userService.registerUser(userDto);
        if(success){
            return new ResponseEntity<>(new ApiResponse("User Registered Successfully", true),HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ApiResponse("Something Went Wrong", false), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(HttpServletRequest request){
        log.info("Get all API Triggered");
        Integer id = jwtTokenHelper.getUserIdFromToken(request);
        log.info("User Id is- "+id);
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id,HttpServletRequest request){
        log.info("Get User By Id API Triggered");
        Integer userId = jwtTokenHelper.getUserIdFromToken(request);
        log.info("User Id is- "+userId);
        return new ResponseEntity<>(userService.getUserById(id),HttpStatus.OK);
    }
    @GetMapping(value = "/",params = "email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email){
        return new ResponseEntity<>(userService.findByEmail(email),HttpStatus.OK);
    }
    @GetMapping(params = "mobile")
    public ResponseEntity<UserDto> getUserByMobile(@RequestParam("mobile") String mobile){
        return new ResponseEntity<>(userService.findByMobile(mobile),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<UserDto>> getByNameKeyword(@RequestParam String name){
        return new ResponseEntity<>(userService.findByNameContaining(name),HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,@PathVariable("id") Integer id){
        return new ResponseEntity<>(userService.updateUser(userDto,id),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Integer id){
        return new ResponseEntity<>(userService.deleteUser(id),HttpStatus.OK);
    }
    @GetMapping("/address")
    public ResponseEntity<List<UserDto>> getUserUsingPinCode(@RequestParam Integer pinCode){
        return new ResponseEntity<>(userService.getUserUsingPinCode(pinCode),HttpStatus.OK);
    }
    @PostMapping("/sendSMS")
    public ResponseEntity<?> sendSMS(@RequestParam("mob") String mob,@RequestParam("msg") String msg){
        log.info("sendSMS API Triggered");
        return new ResponseEntity<>(smsService.sendSMS(mob,msg),HttpStatus.OK);
    }
}
