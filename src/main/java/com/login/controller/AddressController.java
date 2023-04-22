package com.login.controller;

import com.login.dto.AddressDto;
import com.login.service.AddressService;
import com.login.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Integer id){
        return new ResponseEntity<>(addressService.getAddressById(id), HttpStatus.OK);
    }
    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveAddress(@RequestBody AddressDto addressDto){
        boolean success = addressService.saveAddress(addressDto);
        if(success){
            return new ResponseEntity<>(new ApiResponse("Address Registered Successfully", true),HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ApiResponse("Something Went Wrong", false), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
