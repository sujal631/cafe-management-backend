package com.cafe.cafemanagementsystem.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.cafemanagementsystem.constants.CafeConstants;
import com.cafe.cafemanagementsystem.rest.UserRest;
import com.cafe.cafemanagementsystem.service.UserService;
import com.cafe.cafemanagementsystem.utils.CafeUtils;
import com.cafe.cafemanagementsystem.wrapper.UserWrapper;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    // signup
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return this.userService.signUp(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // login
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return this.userService.login(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // get all users by admin
    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            return this.userService.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // update user status from false to true by admin
    @Override
    public ResponseEntity<String> updateUserStatus(Map<String, String> requestMap) {
        try {
            return this.userService.updateUserStatus(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // checking token
    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return this.userService.checkToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            return this.userService.changePassword(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            return this.userService.forgotPassword(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
