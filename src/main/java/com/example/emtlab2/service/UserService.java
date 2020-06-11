package com.example.emtlab2.service;

import com.example.emtlab2.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findById(String username);

    boolean userExists(String username);

    User save(User user);
}
