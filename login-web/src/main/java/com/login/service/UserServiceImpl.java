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
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public UserRepo userRepo;

    @Override
    public boolean registerUser(UserDto userDto) {
        String email = userDto.getEmail();
        if (userRepo.findByEmail(email) != null) {
            throw new UserExistException("User with emailId- " + email + " already exist");
        }
        User user = modelMapper.map(userDto, User.class);
        try {
            userRepo.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Integer id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserDoesNotExistException("User does not exist"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> findByNameContaining(String name) {
        List<User> users = userRepo.findByNameContaining(name);
        if (users == null || users.isEmpty()) {
            throw new UserDoesNotExistException("User Does Not Exist");
        }
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto deleteUser(Integer id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserDoesNotExistException("User Does Not Exist"));
        userRepo.delete(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserDoesNotExistException("User Does Not Exist"));
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        User save = userRepo.save(user);
        return modelMapper.map(save, UserDto.class);
    }
}
