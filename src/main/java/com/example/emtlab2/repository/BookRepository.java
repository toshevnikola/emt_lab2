package com.example.emtlab2.repository;

import com.example.emtlab2.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository  extends JpaRepository<Book, Long> {
    List<Book> findAllByCategoryId(Long categoryId);
    boolean existsByCategoryId(Long categoryId);
}
