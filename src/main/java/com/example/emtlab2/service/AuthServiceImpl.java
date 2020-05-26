package com.example.emtlab2.service;

import com.example.emtlab2.model.User;
import com.example.emtlab2.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        return this.userRepository.findById("emt-user").orElseGet(() -> {
            User user = new User();
            user.setUsername("emt-user");
            return this.userRepository.save(user);
        });
    }

    @Override
    public String getCurrentUserId() {
        return this.getCurrentUser().getUsername();
    }
}
