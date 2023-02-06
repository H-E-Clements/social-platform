package com.example.service;



import com.example.UserDto;
import com.example.domain.Users;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    Users findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
