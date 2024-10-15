package com.dung.UniStore.controller;



import com.dung.UniStore.dto.request.CategoryCreationRequest;
import com.dung.UniStore.dto.response.ApiResponse;

import com.dung.UniStore.entity.Category;

import com.dung.UniStore.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    private final ModelMapper modelMapper;

    @GetMapping
    ApiResponse<List<Category>> getAllCategories() {
        return ApiResponse.<List<Category>>builder()
                .result(categoryService.getAllCategories())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Category> getCategoryById(@PathVariable int id) {
        return ApiResponse.<Category>builder()
                .result(categoryService.getCategoryById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<Category> createCategory(@RequestBody CategoryCreationRequest request) {
        return ApiResponse.<Category>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @PutMapping("{id}")
    public ApiResponse<Category> updateCategory(@PathVariable int id, @RequestBody Category category) {
        category.setId(id);
        return ApiResponse.<Category>builder()
                .result(categoryService.updateCategory(category))
                .build();
    }

    @DeleteMapping("{id}")
    public ApiResponse<String> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder().result("Category has been deleted").build();

    }

}
