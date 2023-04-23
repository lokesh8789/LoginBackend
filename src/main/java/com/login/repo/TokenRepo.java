package com.login.repo;

import com.login.entities.SaveToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<SaveToken,Integer> {
}
