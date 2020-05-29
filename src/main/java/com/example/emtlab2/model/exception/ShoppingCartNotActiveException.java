package com.example.emtlab2.model.exception;

import com.example.emtlab2.model.ShoppingCart;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShoppingCartNotActiveException extends RuntimeException {
    public ShoppingCartNotActiveException(String s) {
        super(String.format("Shopping cart for user %s not active", s));
    }
}
