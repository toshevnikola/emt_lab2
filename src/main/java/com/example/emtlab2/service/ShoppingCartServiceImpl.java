package com.example.emtlab2.service;

import com.example.emtlab2.model.Book;
import com.example.emtlab2.model.ShoppingCart;
import com.example.emtlab2.model.User;
import com.example.emtlab2.model.enumerations.CartStatus;
import com.example.emtlab2.model.exception.ActiveShoppingCartAlreadyExists;
import com.example.emtlab2.model.exception.BookOutOfStockException;
import com.example.emtlab2.model.exception.ShoppingCartNotActiveException;
import com.example.emtlab2.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final BookService bookService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserService userService, BookService bookService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    public List<Book> listItems(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserUsernameAndStatus(username,CartStatus.CREATED);
        return shoppingCart.getBooks();
    }

    @Override
    public ShoppingCart createShoppingCart(String username) {
        User user = userService.findById(username);
        if(this.shoppingCartRepository.existsByUserUsernameAndStatus(username, CartStatus.CREATED)) {
            throw new ActiveShoppingCartAlreadyExists();
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCart;
    }

    @Override
    public ShoppingCart addBookToShoppingCart(String username, Long bookId) {

        ShoppingCart shoppingCart = getActiveShoppingCartOrCreateNew(username);
        Book book = this.bookService.findById(bookId);
        shoppingCart.getBooks().add(book);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeBookFromShoppingCart(String username, Long bookId) {
        ShoppingCart shoppingCart = getActiveShoppingCartOrCreateNew(username);
        shoppingCart.setBooks(
                shoppingCart.getBooks()
                .stream()
                .filter(item -> !item.getId().equals(bookId))
                .collect(Collectors.toList())
        );
        return this.shoppingCartRepository.save(shoppingCart);
    }

    //helper
    private ShoppingCart getActiveShoppingCartOrCreateNew(String username) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        if(shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(userService.findById(username));
            shoppingCart = this.shoppingCartRepository.save(shoppingCart);
        }
        return shoppingCart;
    }
    @Override
    public ShoppingCart cancelShoppingCart(String username) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        if(shoppingCart == null) {
            throw new ShoppingCartNotActiveException();
        }
        shoppingCart.setStatus(CartStatus.CANCELED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart checkoutShoppingCart(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        if (shoppingCart == null) {
            throw new ShoppingCartNotActiveException();
        }
        List<Book> books = shoppingCart.getBooks();
        for (Book b : books) {
            if(b.getQuantity() <= 0) {
                throw new BookOutOfStockException(b.getId());
            }
            b.setQuantity(b.getQuantity() -1);
        }
        shoppingCart.setBooks(books);
        shoppingCart.setStatus(CartStatus.FINISHED);
        return shoppingCartRepository.save(shoppingCart);
    }
}
