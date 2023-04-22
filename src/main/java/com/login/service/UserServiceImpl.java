package com.login.service;

import com.login.dto.UserDto;
import com.login.entities.User;
import com.login.exceptions.UserDoesNotExistException;
import com.login.exceptions.UserExistException;
import com.login.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public UserRepo userRepo;
    @Override
    public boolean registerUser(UserDto userDto){
        String email=userDto.getEmail();
        if (userRepo.findByEmail(email)!=null) {
            throw new UserExistException("User with emailId- "+email+" already exist");
        }
        User user = modelMapper.map(userDto, User.class);
        //user.getAddress().setUser(user);
        User save = userRepo.save(user);
        return save != null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepo.findById(id).orElseThrow(()->new UserDoesNotExistException("User does not exist"));
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if(user==null){
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto findByMobile(String mobile) {
        User user = userRepo.findByMobile(mobile);
        if(user==null){
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public List<UserDto> findByNameContaining(String name) {
        List<User> users = userRepo.findByNameContaining(name);
        if (users == null || users.size()==0) {
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public UserDto deleteUser(Integer id) {
        User user = userRepo.findById(id).orElseThrow(()->new UserDoesNotExistException("User Does Not Exist"));
        userRepo.delete(user);
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto,Integer id) {
        User user = userRepo.findById(id).orElseThrow(()->new UserDoesNotExistException("User Does Not Exist"));
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setMobile(userDto.getMobile());
        user.setPassword(userDto.getPassword());
        User save = userRepo.save(user);
        return modelMapper.map(save,UserDto.class);
    }

    @Override
    public List<UserDto> getUserUsingPinCode(Integer pinCode) {
        List<User> users = userRepo.findByAddressPinCode(pinCode);
        if (users == null || users.size()==0) {
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        return users.stream().map(user -> modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
    }
}
