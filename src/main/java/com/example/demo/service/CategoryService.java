package com.example.demo.service;

import com.example.demo.model.Category;

import java.util.List;

public interface CategoryService {
    Category findByName(String name);
    List<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    Category update(long id , Category category);
    Category updateName(Long id, String name);
    void deleteById(Long id);
}
