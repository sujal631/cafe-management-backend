package com.cafe.cafemanagementsystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cafe.cafemanagementsystem.entity.User;
import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    int updateUserStatus(@Param("status") String status, @Param("id") int id);

    @Query("SELECT u.email FROM User u WHERE u.role = 'admin'")
    List<String> getAllAdmins();

}
