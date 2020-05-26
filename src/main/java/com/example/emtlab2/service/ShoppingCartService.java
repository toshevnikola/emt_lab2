package com.example.emtlab2.service;

import com.example.emtlab2.model.Book;
import com.example.emtlab2.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    List<Book> listItems(String username);
    ShoppingCart createShoppingCart(String username);
    ShoppingCart addBookToShoppingCart(String username, Long bookId);
    ShoppingCart removeBookFromShoppingCart(String username, Long bookId);
    ShoppingCart cancelShoppingCart(String userId);
    ShoppingCart checkoutShoppingCart(String userId);
}
