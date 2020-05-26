package com.example.emtlab2.service;

import com.example.emtlab2.model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    List<Book> findAll();
    List<Book> findAllByCategoryId(Long categoryId);
    Book findById(Long id);
    Book save(Book b, MultipartFile image) throws IOException;
    Book update(Long id, Book book, MultipartFile image) throws IOException;
    void deleteById(Long id);
    boolean existsByCategoryId(Long categoryId);


}
