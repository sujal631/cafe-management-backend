package com.cafe.cafemanagementsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cafe.cafemanagementsystem.entity.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.status = :status WHERE p.product_id = :product_id")
    int updateStatusOfProduct(@Param("status") String status, @Param("product_id") int product_id);

}
