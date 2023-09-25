package com.cafe.cafemanagementsystem.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cafe.cafemanagementsystem.entity.Category;

@RequestMapping("/category")
public interface CategoryRest {

    @PostMapping("/add")
    public ResponseEntity<String> addNewCategory(@RequestBody Map<String, String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam(required = false) String filterValue);

    @PutMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestBody Map<String, String> requestMap);
}
