package com.example.shopbasket.controllers;

import com.example.shopbasket.models.Category;
import com.example.shopbasket.repositories.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/category/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @PostMapping("/category/{id}")
    public Category createCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/category/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @DeleteMapping("/category/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }
}
