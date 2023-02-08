package com.example.reversiblecomputation.service;



import com.example.reversiblecomputation.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getByEmailAddress(email);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmailAddress(),
                    user.getPassword(),
                    AuthorityUtils.createAuthorityList(user.getRoleName())
            );
        }else{
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }
}