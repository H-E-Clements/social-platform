package com.example.reversiblecomputation.service;


import com.example.reversiblecomputation.models.User;
import com.example.reversiblecomputation.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Saves a user to the database.
     * @param user The user to save
     */
    public void saveUser(User user) {
        userRepository.setUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmailAddress(),
                user.getRoleId()
        );
    }

    /**
     * Gets all users that match the given parameters.
     * @return a {@link List} of {@link User}s
     */
    @Transactional
    public List<User> getUsers(
            String id,
            String username,
            String password,
            String fullName,
            String firstName,
            String lastName,
            String emailAddress,
            String roleId
    ) {
        return userRepository.getUsers(id, username, password, fullName, firstName, lastName, emailAddress, roleId);
    }

    /**
     * Gets a single user using the given parameters. If multiple
     * users are present, the first one is returned.
     * @return a {@link User} or `null` if none exists
     */
    @Transactional
    public User getUser(
            String id,
            String username,
            String password,
            String fullName,
            String firstName,
            String lastName,
            String emailAddress,
            String roleId
    ) {
        List<User> users = this.getUsers(id, username, password, fullName, firstName, lastName, emailAddress, roleId);
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    /**
     * Deletes all users that match the given parameters
     */
    public void delUsers(
            String id,
            String username,
            String password,
            String fullName,
            String firstName,
            String lastName,
            String emailAddress,
            String roleId
    ) {
        userRepository.delUsers(id, username, password, fullName, firstName, lastName, emailAddress, roleId);
    }

    /**
     * Gets a list of all users.
     * @return a {@link List} of all {@link User}s
     */
    @Transactional
    public List<User> getAllUsers() {
        return this.getUsers(null, null, null, null, null, null, null, null);
    }

    /**
     * Gets a user by their username.
     * @param username the username to use
     * @return a {@link User} or `null` if none exists
     */
    @Transactional
    public User getByUsername(String username) {
        return this.getUser(null, username, null, null, null, null, null, null);
    }

    /**
     * Gets a user by their email address.
     * @param emailAddress the email address to use
     * @return a {@link User} or `null` if none exists
     */
    @Transactional
    public User getByEmailAddress(String emailAddress) {
        return this.getUser(null, null, null, null, null, null, emailAddress, null);
    }
}
