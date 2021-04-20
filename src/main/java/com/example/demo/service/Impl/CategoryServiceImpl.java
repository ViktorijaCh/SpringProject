package com.example.demo.service.Impl;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryServiceImpl implements CategoryService {
    public final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();

    }

//    @Override
//    public Category findByName(String name) {
//        return categoryRepository.findByName(name);
//
//    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findById(id).orElseThrow(()->new NoSuchElementException());

    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);

    }

    @Override
    public Category update(long id, Category category) {
        Category k = this.findById(id);
        k.setName(category.getName());
        return this.categoryRepository.save(k);
    }

    @Override
    public Category updateName(Long id, String name) {
        Category k = this.findById(id);
        k.setName(name);
        return this.categoryRepository.save(k);
    }

    @Override
    public void deleteById(Long id) {
        this.categoryRepository.deleteById(id);
    }
}
