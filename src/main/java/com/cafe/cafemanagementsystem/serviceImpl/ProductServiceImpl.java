package com.cafe.cafemanagementsystem.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.cafemanagementsystem.JWT.JwtAuthenticationFilter;
import com.cafe.cafemanagementsystem.constants.CafeConstants;
import com.cafe.cafemanagementsystem.entity.Category;
import com.cafe.cafemanagementsystem.entity.Product;
import com.cafe.cafemanagementsystem.repo.ProductRepository;
import com.cafe.cafemanagementsystem.service.ProductService;
import com.cafe.cafemanagementsystem.utils.CafeUtils;
import com.cafe.cafemanagementsystem.wrapper.ProductWrapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            // if user is admin or not
            if (jwtAuthenticationFilter.isAdmin()) {
                if (validateProductMap(requestMap, false)) {
                    this.productRepository.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity(CafeConstants.ADD_PRODUCT_SUCCESSFUL, HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_USER, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProducts() {
        try {
            List<Product> products = this.productRepository.findAll();
            List<ProductWrapper> productWrappers = new ArrayList<>();
            for (Product product : products) {
                ProductWrapper productWrapper = new ProductWrapper();
                BeanUtils.copyProperties(product, productWrapper);
                // Set category_id and category_name in wrapper from the product's category
                if (product.getCategory() != null) {
                    productWrapper.setCategory_id(product.getCategory().getCategory_id());
                    productWrapper.setCategory_name(product.getCategory().getCategory_name());
                }
                productWrappers.add(productWrapper);
            }
            return new ResponseEntity<List<ProductWrapper>>(productWrappers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtAuthenticationFilter.isAdmin()) {
                if (validateProductMap(requestMap, true)) {
                    Optional<Product> optional = this.productRepository
                            .findById(Integer.parseInt(requestMap.get("product_id")));
                    if (optional.isPresent()) {
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        this.productRepository.save(product);
                        return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_UPDATE_SUCCESSFUL, HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_DOES_NOT_EXIST,
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

    @Override
    public ResponseEntity<String> deleteProduct(int product_id) {
        try {
            log.info("Inside delete product: {}", product_id);
            if (jwtAuthenticationFilter.isAdmin()) {
                Optional<Product> optional = this.productRepository.findById(product_id);
                if (optional.isPresent()) {
                    this.productRepository.deleteById(product_id);
                    return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_DELETE_SUCCESSFUL, HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_DOES_NOT_EXIST,
                        HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_USER, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatusOfProduct(Map<String, String> requestMap) {
        try {
            if (jwtAuthenticationFilter.isAdmin()) {
                Optional<Product> optional = this.productRepository
                        .findById(Integer.parseInt(requestMap.get("product_id")));
                if (optional.isPresent()) {
                    this.productRepository.updateStatusOfProduct(requestMap.get("status"),
                            Integer.parseInt(requestMap.get("product_id")));
                    return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_STATUS_UPDATE_SUCCESSFUL, HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_DOES_NOT_EXIST,
                        HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_USER, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductsBtCategory(int category_id) {
        try {
            List<Product> products = this.productRepository.findAll();
            List<ProductWrapper> productWrappers = new ArrayList<>();
            for (Product product : products) {
                // show only the products whose status is true and whose category_id matches
                // with the category_id we are passing
                if (product.getCategory().getCategory_id() == category_id
                        && product.getStatus().equalsIgnoreCase("true")) {
                    ProductWrapper productWrapper = new ProductWrapper();
                    BeanUtils.copyProperties(product, productWrapper);
                    // Set category_id and category_name in wrapper from the product's category
                    if (product.getCategory() != null) {
                        productWrapper.setCategory_id(product.getCategory().getCategory_id());
                        productWrapper.setCategory_name(product.getCategory().getCategory_name());
                    }
                    productWrappers.add(productWrapper);
                }
            }
            return new ResponseEntity<List<ProductWrapper>>(productWrappers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getProduct(int product_id) {
        try {
            Optional<Product> optional = this.productRepository.findById(product_id);
            if (optional.isPresent()) {
                Product product = optional.get();
                ProductWrapper productWrapper = new ProductWrapper();
                BeanUtils.copyProperties(product, productWrapper);
                productWrapper.setCategory_id(product.getCategory().getCategory_id());
                productWrapper.setCategory_name(product.getCategory().getCategory_name());

                return new ResponseEntity<ProductWrapper>(productWrapper, HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_DOES_NOT_EXIST,
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("product_name") && requestMap.containsKey("product_description")
                && requestMap.containsKey("price") && requestMap.containsKey("category_id")) {
            if (requestMap.containsKey("product_id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdded) {
        Category category = new Category();
        category.setCategory_id(Integer.parseInt(requestMap.get("category_id")));
        Product product = new Product();
        if (isAdded) {
            product.setProduct_id(Integer.parseInt(requestMap.get("product_id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setProduct_name(requestMap.get("product_name"));
        product.setProduct_description(requestMap.get("product_description"));
        product.setPrice(Double.parseDouble(requestMap.get("price")));
        return product;
    }

}
