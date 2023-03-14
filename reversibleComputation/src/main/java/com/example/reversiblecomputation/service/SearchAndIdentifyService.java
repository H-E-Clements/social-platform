package com.example.reversiblecomputation.service;

import com.example.reversiblecomputation.domain.User;
import com.example.reversiblecomputation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SearchAndIdentifyService {

    @Autowired
    private UserRepository userRepository;

    public boolean checkIfLoggedIn(Authentication authentication){
        boolean user;
        try {
            authentication.getName();
            user = true;
        }
        catch(Exception e) {
            user = false;
        }

        return user;
    }

    public boolean checkImg(Authentication authentication){
        if (checkIfLoggedIn(authentication)) {
            String name = authentication.getName();
            User user = userRepository.findByEmail(name);
            if (user.isImg()){
                return true;
            }

        }
        return false;
    }

    public User userObject(Authentication authentication){
        if (checkIfLoggedIn(authentication)) {
            String name = authentication.getName();
            User user = userRepository.findByEmail(name);
            return user;
        }
        return null;
    }

    public User searchUserObjectId(Authentication authentication, Long id){
        try {
            User userObject;
            Optional<User> user = userRepository.findById(id);
            userObject = user.get();
            return userObject;
        }
        catch(Exception e) {
            return null;
        }
    }
}
