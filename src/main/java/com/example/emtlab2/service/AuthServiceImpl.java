package com.example.emtlab2.service;

import com.example.emtlab2.model.User;
import com.example.emtlab2.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getCurrentUserId() {
        return this.getCurrentUser().getUsername();
    }
}
