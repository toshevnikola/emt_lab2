package com.example.emtlab2.service;

import com.example.emtlab2.model.User;

public interface AuthService {
    User getCurrentUser();
    String getCurrentUserId();
}
