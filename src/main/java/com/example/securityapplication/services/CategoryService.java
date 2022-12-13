package com.example.securityapplication.services;

import com.example.securityapplication.models.Category;
import com.example.securityapplication.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public Category findCategoryById(int id){
        Optional<Category> category = categoryRepository.findCategoryById(id);
        return category.orElse(null);
    }
    public Category findByName(String name){
        Optional<Category> category = categoryRepository.findByName(name);
        return category.orElse(null);
    }
}
