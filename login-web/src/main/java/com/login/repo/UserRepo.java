package com.login.repo;

import com.login.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    List<User> findByNameContaining(String name);

    @Query("select id from User where email= :email and password = :password")
    Integer findIdByEmailAndPassword(String email, String password);
}
