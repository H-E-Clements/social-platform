package com.example.repository;

import com.example.domain.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<Users, String> {
    Users findByName(String name);

    Users findByEmail(String email);

}
