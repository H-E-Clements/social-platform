package com.example.reversiblecomputation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        // tells which pages need authorisation
                        authorize
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/feed").permitAll()
                                .requestMatchers("/test").permitAll()
                                .requestMatchers("/search").permitAll()
                                .requestMatchers("/username").permitAll()
                                .requestMatchers("/profile/**").permitAll()
                                .requestMatchers("/upload").permitAll()
                                .requestMatchers("/newDocument").permitAll()
                                .requestMatchers("/uploadimage").permitAll()
                                .requestMatchers("/upload").permitAll()
                                .requestMatchers("/uploadPaper").permitAll()
                                .requestMatchers("/viewPapers").permitAll()
                                .requestMatchers("/viewPaper/**").permitAll()
                                .requestMatchers("/post").permitAll()
                                .requestMatchers("/search/**").permitAll()
                                .requestMatchers("/createPost").permitAll()
                                .requestMatchers("/feed").permitAll()
                                .requestMatchers("/pfp/**").permitAll()
                                .requestMatchers("/createComment/**").permitAll()
                                .requestMatchers("/events").hasRole("ADMIN")
                                .requestMatchers("/users").hasRole("ADMIN")
                                .requestMatchers("/edit/**").hasRole("ADMIN")
                                .requestMatchers("/post").hasRole("ADMIN")
                ).formLogin(
                        form -> form
                                .loginPage("/login").permitAll()
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
