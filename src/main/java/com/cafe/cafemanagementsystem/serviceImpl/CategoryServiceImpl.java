package com.cafe.cafemanagementsystem.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.cafemanagementsystem.JWT.JwtAuthenticationFilter;
import com.cafe.cafemanagementsystem.constants.CafeConstants;
import com.cafe.cafemanagementsystem.entity.Category;
import com.cafe.cafemanagementsystem.repo.CategoryRepository;
import com.cafe.cafemanagementsystem.service.CategoryService;
import com.cafe.cafemanagementsystem.utils.CafeUtils;
import com.google.common.base.Strings;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // adding a new Category
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            // only admin can add category which is why checking if user is admin
            if (jwtAuthenticationFilter.isAdmin()) {
                if (validateCategoryMap(requestMap, false)) {
                    this.categoryRepository.save(getCategoryFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity(CafeConstants.ADD_CATEGORY_SUCCESSFUL, HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_USER, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("category_name")) {
            if (requestMap.containsKey("category_id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdded) {
        Category category = new Category();
        if (isAdded) {
            category.setCategory_id(Integer.parseInt(requestMap.get("category_id")));
        }
        category.setCategory_name(requestMap.get("category_name"));
        return category;
    }

    // get all categories
    @Override
    public ResponseEntity<List<Category>> getAllCategories(String filterValue) {
        try {
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Category>>(this.categoryRepository.getAllCategories(), HttpStatus.OK);
            }
            return new ResponseEntity<List<Category>>(this.categoryRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (jwtAuthenticationFilter.isAdmin()) {
                if (validateCategoryMap(requestMap, true)) {
                    Optional<Category> optional = this.categoryRepository
                            .findById(Integer.parseInt(requestMap.get("category_id")));
                    if (optional.isPresent()) {
                        this.categoryRepository.save(getCategoryFromMap(requestMap, true));
                        return CafeUtils.getResponseEntity(CafeConstants.CATEGORY_UPDATE_SUCCESSFUL, HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity(CafeConstants.CATEGORY_DOES_NOT_EXIST,
                                HttpStatus.BAD_REQUEST);
                    }
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_USER, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
