package com.example.emtlab2.presentation.controllers;

import com.example.emtlab2.model.Book;
import com.example.emtlab2.model.ShoppingCart;
import com.example.emtlab2.model.dto.ChargeRequest;
import com.example.emtlab2.service.AuthService;
import com.example.emtlab2.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/charge")
public class PaymentController {
    @Value("${STRIPE_P_KEY}")
    private String publicKey;

    private ShoppingCartService shoppingCartService;
    private AuthService authService;

    public PaymentController(ShoppingCartService shoppingCartService, AuthService authService) {
        this.shoppingCartService = shoppingCartService;
        this.authService = authService;
    }

    @GetMapping
    public String getChargePage(Model model) {
        try {
            ShoppingCart activeShoppingCartByUsername = this.shoppingCartService.findActiveShoppingCartByUsername(this.authService.getCurrentUserId());
            model.addAttribute("shoppingCart", activeShoppingCartByUsername);
            model.addAttribute("currency", "eur");
            model.addAttribute("amount", ((int) activeShoppingCartByUsername.getBooks().stream().mapToDouble(Book::getPrice).sum()) * 100);
            model.addAttribute("stripePublicKey", this.publicKey);
        } catch (RuntimeException e) {
            return "redirect:/books?error=Error";
        }
        return "charge";
    }

    @PostMapping
    public String checkout(ChargeRequest chargeRequest, Model model) {
        try {
            this.shoppingCartService.checkoutShoppingCart(this.authService.getCurrentUserId(), chargeRequest);
            return "redirect:/books?message=Successful Payment";
        } catch (RuntimeException ex) {
            return "redirect:/charge/error=" + ex.getMessage();
        }
    }
}
