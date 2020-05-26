package com.example.emtlab2.presentation.controllers;

import com.example.emtlab2.model.Book;
import com.example.emtlab2.model.Category;
import com.example.emtlab2.service.BookService;
import com.example.emtlab2.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private BookService bookService;
    private CategoryService categoryService;
    public BookController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String ListBooksPage(Model model) {
        List<Book> books = this.bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @PostMapping
    public String saveOrUpdateBook(Model model, @Valid Book book, BindingResult bindingResult, @RequestParam MultipartFile image) throws IOException {
        if(bindingResult.hasErrors()) {
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            return "add-book";

        }
        try {
            if(book.getId() == null) {
                this.bookService.save(book, image);
            }
            else{
                this.bookService.update(book.getId(), book, image);
            }
        } catch (RuntimeException ex) {
            return "redirect:/books/add-new?error=" + "Problem";
        }
        return "redirect:/books";
    }

    @GetMapping("/add-new")
    public String addNewBookPage(Model model) {
        List<Category> categories = this.categoryService.findAll();
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categories);
        return "add-book";
    }
    @GetMapping("/{id}/edit")
    public String editBookPage(@PathVariable Long id, Model model) {
        try {
            Book book = this.bookService.findById(id);
            List<Category> categories = this.categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("book", book);
            return "add-book";
        }catch (RuntimeException ex) {
            return "redirect:/books?error=" + ex.getLocalizedMessage();
        }

    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        this.bookService.deleteById(id);
        return "redirect:/books";
    }


}
