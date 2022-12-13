package com.example.securityapplication.repositories;

import com.example.securityapplication.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {


    Optional<Category> findCategoryById(int id);

    Optional<Category> findByName(String name);
    void deleteById(Integer integer);
}
