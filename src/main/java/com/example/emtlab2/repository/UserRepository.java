package com.example.emtlab2.repository;

import com.example.emtlab2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
