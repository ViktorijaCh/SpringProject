package com.example.demo.service;


import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findById(String username);
    User registerUser(User user);
    boolean existsById(String id);
    User save(User user);

    void like(Long id, User currentUser);
}
