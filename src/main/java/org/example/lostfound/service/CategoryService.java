package org.example.lostfound.service;

import org.example.lostfound.mapper.CategoryMapper;
import org.example.lostfound.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> getAllCategories() {
        return categoryMapper.getAllCategories();
    }

    public Category getCategoryById(Integer categoryId) {
        return categoryMapper.getCategoryById(categoryId);
    }

    public Category getCategoryByName(String name) {
        return categoryMapper.getCategoryByName(name);
    }
}
