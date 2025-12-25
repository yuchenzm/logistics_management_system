package com.logistics.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 客户实体类
 */
@Data
public class Customer {
    private Long id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String customerType; // individual, company
    private String creditRating; // A, B, C, D
    private String status; // active, inactive
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 