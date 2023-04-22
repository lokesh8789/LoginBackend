package com.login.repo;

import com.login.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address,Integer> {
    Address findByUserEmail(String email);
}
