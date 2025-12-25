package com.logistics.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 司机实体类
 */
@Data
public class Driver {
    private Long id;
    private String name;
    private String licenseNumber;
    private String phone;
    private String email;
    private String address;
    private LocalDate hireDate;
    private String licenseType;
    private Integer experienceYears;
    private BigDecimal rating;
    private String status; // available, busy, off_duty, inactive
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 