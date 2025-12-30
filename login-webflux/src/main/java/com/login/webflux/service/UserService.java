package com.login.webflux.service;

import com.login.webflux.dto.UserDto;
import com.login.webflux.entity.User;
import com.login.webflux.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public Mono<UserDto> getUserById(int id) {
        return userRepo.findById(id)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    public Mono<UserDto> getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    public Flux<UserDto> getAllUsers() {
        return userRepo.findAll()
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    public Flux<UserDto> getUsersWithNameContains(String name) {
        return userRepo.findByNameContaining(name)
                .map(user -> modelMapper.map(user, UserDto.class));
    }
}
