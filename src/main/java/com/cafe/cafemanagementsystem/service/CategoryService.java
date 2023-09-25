package com.cafe.cafemanagementsystem.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cafe.cafemanagementsystem.entity.Category;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);

    ResponseEntity<List<Category>> getAllCategories(String filterValue);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);

}
