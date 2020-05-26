package com.example.emtlab2.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class BookWithThisCategoryExists extends RuntimeException {
    public BookWithThisCategoryExists(Long id) {
        super(String.format("Product with category %d exists", id));
    }
}
