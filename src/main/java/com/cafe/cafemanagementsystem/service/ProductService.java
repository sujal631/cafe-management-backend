package com.cafe.cafemanagementsystem.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cafe.cafemanagementsystem.wrapper.ProductWrapper;

public interface ProductService {

    ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

    ResponseEntity<List<ProductWrapper>> getAllProducts();

    ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    ResponseEntity<String> deleteProduct(int product_id);

    ResponseEntity<String> updateStatusOfProduct(Map<String, String> requestMap);

    ResponseEntity<List<ProductWrapper>> getProductsBtCategory(int category_id);

    ResponseEntity<?> getProduct(int product_id);

}
