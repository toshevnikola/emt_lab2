package com.example.emtlab2.service;

import com.example.emtlab2.model.User;
import com.example.emtlab2.model.exception.UserNotFoundException;
import com.example.emtlab2.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(String username) {
        return userRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));
    }
}
