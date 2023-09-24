package com.cafe.cafemanagementsystem.wrapper;

import lombok.Data;

@Data
public class UserWrapper {
    private int id;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private String status;

}
