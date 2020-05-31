package com.example.emtlab2.service;

import com.example.emtlab2.model.Book;
import com.example.emtlab2.model.ShoppingCart;
import com.example.emtlab2.model.Transaction;
import com.example.emtlab2.model.User;
import com.example.emtlab2.model.dto.ChargeRequest;
import com.example.emtlab2.model.enumerations.CartStatus;
import com.example.emtlab2.model.exception.*;
import com.example.emtlab2.repository.ShoppingCartRepository;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final BookService bookService;
    private PaymentService paymentService;
    private TransactionService transactionService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserService userService, BookService bookService, PaymentService paymentService, TransactionService transactionService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.bookService = bookService;
        this.paymentService = paymentService;
        this.transactionService = transactionService;
    }

    @Override
    public List<Book> listItems(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        return shoppingCart.getBooks();
    }

    @Override
    public ShoppingCart createShoppingCart(String username) {
        User user = userService.findById(username);
        if (this.shoppingCartRepository.existsByUserUsernameAndStatus(username, CartStatus.CREATED)) {
            throw new ActiveShoppingCartAlreadyExists();
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        this.shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public ShoppingCart addBookToShoppingCart(String username, Long bookId) {

        ShoppingCart shoppingCart = getActiveShoppingCartOrCreateNew(username);
        Book book = this.bookService.findById(bookId);
        for (Book b : shoppingCart.getBooks()) {
            if (b.getId().equals(bookId)) {
                throw new BookAlreadyInCartException(bookId);
            }
        }

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
        if (shoppingCart.getBooks().isEmpty()) {
            shoppingCart.setStatus(CartStatus.CANCELED);
        }
        return this.shoppingCartRepository.save(shoppingCart);
    }

    //helper
    private ShoppingCart getActiveShoppingCartOrCreateNew(String username) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(userService.findById(username));
            shoppingCart = this.shoppingCartRepository.save(shoppingCart);
        }
        return shoppingCart;
    }

    @Override
    public ShoppingCart cancelShoppingCart(String username) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        if (shoppingCart == null) {
            throw new ShoppingCartNotActiveException(username);
        }
        shoppingCart.setStatus(CartStatus.CANCELED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart checkoutShoppingCart(String username, ChargeRequest chargeRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        if (shoppingCart == null) {
            throw new ShoppingCartNotActiveException(username);
        }
        List<Book> books = shoppingCart.getBooks();
        float price = 0;
        for (Book b : books) {
            if (b.getQuantity() <= 0) {
                shoppingCart.getBooks().remove(b);
                throw new BookOutOfStockException(b.getId());
            }
            b.setQuantity(b.getQuantity() - 1);
            price += b.getPrice();
        }
        Charge charge = null;
        try {
            charge = this.paymentService.pay(chargeRequest);
        } catch (CardException | APIException | AuthenticationException | InvalidRequestException | APIConnectionException e) {
            throw new TransactionFailedException(username, e.getMessage());
        }

        Transaction t = new Transaction();
        t.setSenderUser(userService.findById(username));
        t.setId(charge.getId());
        t.setObject(charge.getObject());
        t.setAmount(charge.getAmount());
        t.setAmountRefunded(charge.getAmountRefunded());
        t.setApplication(charge.getApplication());
        t.setApplicationFee(charge.getApplicationFee());
        t.setCaptured(charge.getCaptured());
        t.setCreated(charge.getCreated());
        t.setCurrency(charge.getCurrency());
        t.setDescription(charge.getDescription());
        t.setPaid(charge.getPaid());
        t.setRefunded(charge.getRefunded());
        t.setStatus(charge.getStatus());
        t.setDisputed(charge.getDispute());

        this.transactionService.save(t);
        shoppingCart.setBooks(books);
        shoppingCart.setStatus(CartStatus.FINISHED);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart findActiveShoppingCartByUsername(String userId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(userId, CartStatus.CREATED);
        if (shoppingCart == null)
            throw new ShoppingCartNotActiveException(userId);
        return this.shoppingCartRepository.findByUserUsernameAndStatus(userId, CartStatus.CREATED);
    }
}
