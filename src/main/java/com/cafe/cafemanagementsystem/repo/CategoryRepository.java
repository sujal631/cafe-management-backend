package com.cafe.cafemanagementsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.cafemanagementsystem.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
