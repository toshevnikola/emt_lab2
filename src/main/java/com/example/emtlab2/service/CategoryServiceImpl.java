package com.example.emtlab2.service;

import com.example.emtlab2.model.Category;
import com.example.emtlab2.model.exception.BookWithThisCategoryExists;
import com.example.emtlab2.model.exception.CategoryNotFoundException;
import com.example.emtlab2.repository.BookRepository;
import com.example.emtlab2.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category save(Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        Category updatedCategory = this.findById(id);
        updatedCategory.setName(category.getName());
        updatedCategory.setDescription(category.getDescription());
        return categoryRepository.save(updatedCategory);
    }

    @Override
    public Integer deleteById(Long id) {
        Category category = this.findById(id);
        if(bookRepository.existsByCategoryId(id)) {
            throw new BookWithThisCategoryExists(id);
        }
        return this.categoryRepository.removeById(id);
    }
}
