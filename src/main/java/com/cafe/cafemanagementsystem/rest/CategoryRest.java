package com.cafe.cafemanagementsystem.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/category")
public interface CategoryRest {

    @PostMapping("/add")
    public ResponseEntity<String> addNewCategory(@RequestBody Map<String, String> requestMap);
}
