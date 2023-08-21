package com.example.jwtdemo.repository;

import com.example.jwtdemo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // finding user by email
    Optional<User> findByEmail(String email);
}
