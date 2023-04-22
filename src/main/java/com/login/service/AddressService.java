package com.login.service;

import com.login.dto.AddressDto;

public interface AddressService {
    AddressDto getAddressById(Integer id);
    boolean saveAddress(AddressDto addressDto);
}
