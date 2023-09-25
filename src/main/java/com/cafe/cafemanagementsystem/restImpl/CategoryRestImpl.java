package com.cafe.cafemanagementsystem.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.cafemanagementsystem.constants.CafeConstants;
import com.cafe.cafemanagementsystem.rest.CategoryRest;
import com.cafe.cafemanagementsystem.service.CategoryService;
import com.cafe.cafemanagementsystem.utils.CafeUtils;

@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            return this.categoryService.addNewCategory(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
