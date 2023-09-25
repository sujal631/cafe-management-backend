package com.cafe.cafemanagementsystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cafe.cafemanagementsystem.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.category_id IN(SELECT p.category FROM Product p WHERE p.status = 'true')")
    List<Category> getAllCategories();

}
