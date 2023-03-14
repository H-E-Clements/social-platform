package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    Optional<User> findById(Long id);

    List<User> findByNameContainingIgnoreCase(String word);

    List<User> findAllById(Long userId);


}