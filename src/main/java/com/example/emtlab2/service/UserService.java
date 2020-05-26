package com.example.emtlab2.service;

import com.example.emtlab2.model.User;

public interface UserService {
    User findById(String username);
}
