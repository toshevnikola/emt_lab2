package com.example.emtlab2.service;

import com.example.emtlab2.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    Category update(Long id, Category category);
    Integer deleteById(Long id);
}
