package com.cafe.cafemanagementsystem.wrapper;

import lombok.Data;

@Data
public class ProductWrapper {
    private int product_id;
    private String product_name;
    private String product_description;
    private String status;
    private double price;
    private int category_id;
    private String category_name;
}
