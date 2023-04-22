package com.login.service;

import com.login.dto.AddressDto;
import com.login.entities.Address;
import com.login.entities.User;
import com.login.exceptions.ResourceNotFoundException;
import com.login.exceptions.UserExistException;
import com.login.repo.AddressRepo;
import com.login.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;
    @Override
    public AddressDto getAddressById(Integer id) {
        Address address = addressRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address", "Id", id));
        return modelMapper.map(address,AddressDto.class);
    }

    @Override
    public boolean saveAddress(AddressDto addressDto) {
        String email=addressDto.getUserDto().getEmail();
        if(addressRepo.findByUserEmail(email)!=null){
            throw new UserExistException("User with emailId- "+email+" already exist");
        }
        Address address = modelMapper.map(addressDto, Address.class);
        User user = userRepo.save(address.getUser());
        address.setUser(user);
        Address save = addressRepo.save(address);
        return save!=null;
    }
}
