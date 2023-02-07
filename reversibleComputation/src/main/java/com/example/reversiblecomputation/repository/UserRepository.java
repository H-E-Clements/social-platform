package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}