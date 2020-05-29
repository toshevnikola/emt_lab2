package com.example.emtlab2.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class BookAlreadyInCartException extends RuntimeException {
    public BookAlreadyInCartException(Long bookId) {
        super(String.format("Book with id %d already in cart", bookId));
    }
}
