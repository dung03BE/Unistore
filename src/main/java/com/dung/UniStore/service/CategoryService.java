package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.CategoryCreationRequest;
import com.dung.UniStore.dto.response.RoleResponse;
import com.dung.UniStore.entity.Category;
import com.dung.UniStore.repository.ICategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService{
    final ICategoryRepository categoryRepository;
    final ModelMapper modelMapper;
    @Override
    public List<Category> getAllCategories() {
        List<Category> listCategories = categoryRepository.findAll();
        return listCategories ;
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Category createCategory(CategoryCreationRequest request) {
        Category category =modelMapper.map(request,Category.class);
       return  categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }
}
