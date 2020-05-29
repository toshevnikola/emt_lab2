package com.example.emtlab2.presentation.controllers;

import com.example.emtlab2.service.AuthService;
import com.example.emtlab2.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart/books")
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;
    private AuthService authService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @PostMapping("/add/{id}")
    public String addProductToCart(@PathVariable(name = "id") Long id) {
        try {

            this.shoppingCartService.addBookToShoppingCart(this.authService.getCurrentUserId(), id);
            return "redirect:/books";
        } catch (RuntimeException ex) {
            return "redirect:/books?error=" + ex.getLocalizedMessage();
        }
    }

    @PostMapping("/remove/{id}")
    public String deleteProductToCart(@PathVariable(name = "id") Long id) {
        this.shoppingCartService.removeBookFromShoppingCart(this.authService.getCurrentUserId(), id);
        return "redirect:/books";
    }
}
