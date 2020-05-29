package com.example.emtlab2.presentation.controllers.rest;

import com.example.emtlab2.model.Book;
import com.example.emtlab2.model.ShoppingCart;
import com.example.emtlab2.service.AuthService;
import com.example.emtlab2.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartRestController {
    private final ShoppingCartService shoppingCartService;
    private final AuthService authService;

    public ShoppingCartRestController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping
    public List<Book> listItems() {
        return shoppingCartService.listItems(this.authService.getCurrentUserId());
    }

    @PostMapping
    public ShoppingCart createShoppingCart() {
        return this.shoppingCartService.createShoppingCart(this.authService.getCurrentUserId());
    }

    @PatchMapping("/{bookId}/books")
    public ShoppingCart addBookToShoppingCart(@PathVariable Long bookId) {
        return this.shoppingCartService.addBookToShoppingCart(this.authService.getCurrentUserId(), bookId);
    }

    @DeleteMapping("/{bookId}/books")
    public ShoppingCart removeBookFromShoppingCart(@PathVariable Long bookId) {
        return this.shoppingCartService.removeBookFromShoppingCart(this.authService.getCurrentUserId(), bookId);
    }

    @DeleteMapping
    public ShoppingCart cancelShoppingCart() {
        return this.shoppingCartService.cancelShoppingCart(this.authService.getCurrentUserId());
    }

    @PostMapping("/checkout")
    public ShoppingCart checkoutShoppingCart() {
        return null;
    }


}
