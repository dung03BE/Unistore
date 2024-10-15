package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.CategoryCreationRequest;
import com.dung.UniStore.dto.response.RoleResponse;
import com.dung.UniStore.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(int id);

    Category createCategory(CategoryCreationRequest request);

    Category updateCategory(Category category);

    void deleteCategory(int id);
}
