package com.example;

import com.example.domain.Roles;
import com.example.domain.Users;
import com.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ReversibleComputationApplication implements ApplicationRunner {

    @Autowired
    private UsersRepository repo;

    public static void main(String[] args) {
        SpringApplication.run(ReversibleComputationApplication.class, args);
    }

    @Autowired
    private PasswordEncoder pe;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

}
