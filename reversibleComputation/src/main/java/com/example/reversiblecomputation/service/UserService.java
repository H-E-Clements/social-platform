package com.example.reversiblecomputation.service;



import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.dto.Dto;

import java.util.List;

public interface UserService {
    void saveUser(Dto userDto);

    User findUserByEmail(String email);

    List<Dto> findAllUsers();
}