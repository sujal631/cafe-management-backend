package com.cafe.cafemanagementsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.cafemanagementsystem.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}
