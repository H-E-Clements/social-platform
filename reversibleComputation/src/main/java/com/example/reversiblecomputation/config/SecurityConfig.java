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

        //SecurityConfig includes code for password encryption and also includes permissions under which pages can be accessed

        //userDetailsService and passwordEncoder imported for later use (password encryption)
    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

        //SecurityFilterChain handles request authorisation
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                //authorizeHttpRequests tells the application which permissions are required to access certain requests
                .authorizeHttpRequests((authorize) ->
                        // tells which pages need authorisation
                        authorize
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/feed").permitAll()
                                .requestMatchers("/playground").permitAll()
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

                                .requestMatchers("/newEvent").permitAll()
                                .requestMatchers("/createEvent").permitAll()
                                .requestMatchers("/events").permitAll()

                                .requestMatchers("/simulator").permitAll()
                //formLogin helps with handling login requests, this code tells it where login requests are to be processed and also defines the URL that the user is redirected to after a successful login
                ).formLogin(
                        form -> form
                                .loginPage("/login").permitAll()
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                //logout helps with handling logout requests, this code tells it where logout requests are to be processed
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

        //This code configures the password encoder to be used within the Authentication build
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
