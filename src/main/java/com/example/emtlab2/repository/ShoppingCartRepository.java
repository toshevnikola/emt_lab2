package com.example.emtlab2.repository;

import com.example.emtlab2.model.ShoppingCart;
import com.example.emtlab2.model.enumerations.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByUserUsernameAndStatus(String username, CartStatus status);
    boolean existsByUserUsernameAndStatus(String username, CartStatus status);

}
