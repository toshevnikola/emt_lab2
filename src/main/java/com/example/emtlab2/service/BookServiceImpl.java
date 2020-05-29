package com.example.emtlab2.service;

import com.example.emtlab2.model.Book;
import com.example.emtlab2.model.Category;
import com.example.emtlab2.model.exception.BookNotFoundException;
import com.example.emtlab2.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Book> findAll() {
        return this.bookRepository.findAll();
    }

    @Override
    public List<Book> findAllByCategoryId(Long categoryId) {
        return this.bookRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public Book findById(Long id) {
        return this.bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public Book save(Book b, MultipartFile image) throws IOException {
        Category category = this.categoryService.findById(b.getCategory().getId());
        b.setCategory(category);

        if (image != null) {
            byte[] imageBytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(imageBytes));
            b.setBase64Image(base64Image);
        }
        return this.bookRepository.save(b);
    }

    @Override
    public Book update(Long id, Book book, MultipartFile image) throws IOException {
        Book updatedBook = this.findById(id);
        Category category = this.categoryService.findById(book.getCategory().getId());

        updatedBook.setCategory(category);
        updatedBook.setTitle(book.getTitle());
        updatedBook.setQuantity(book.getQuantity());
        updatedBook.setPrice(book.getPrice());
        if (image != null) {
            byte[] imageBytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(imageBytes));
            updatedBook.setBase64Image(base64Image);
        }

        return this.bookRepository.save(updatedBook);
    }

    @Override
    public void deleteById(Long id) {
        this.bookRepository.deleteById(id);
    }

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return this.bookRepository.existsByCategoryId(categoryId);
    }
}
