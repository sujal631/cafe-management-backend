package com.cafe.cafemanagementsystem.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cafe.cafemanagementsystem.wrapper.UserWrapper;

@RequestMapping("/user")
public interface UserRest {

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody Map<String, String> requestMap);

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>> getAllUsers();

    @PutMapping("/update")
    public ResponseEntity<String> updateUserStatus(@RequestBody Map<String, String> requestMap);

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken();

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap);

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestMap);
}
