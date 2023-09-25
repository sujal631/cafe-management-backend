package com.cafe.cafemanagementsystem.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.cafemanagementsystem.wrapper.ProductWrapper;

@RequestMapping("/product")
public interface ProductRest {

    @PostMapping("/add")
    public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<ProductWrapper>> getAllProducts();

    @PutMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody Map<String, String> requestMap);

    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int product_id);

    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateStatusOfProduct(@RequestBody Map<String, String> requestMap);

    @GetMapping("/getByCategory/{category_id}")
    public ResponseEntity<List<ProductWrapper>> getProductsByCategory(@PathVariable int category_id);

    @GetMapping("/get/{product_id}")
    public ResponseEntity<?> getProduct(@PathVariable int product_id);
}
