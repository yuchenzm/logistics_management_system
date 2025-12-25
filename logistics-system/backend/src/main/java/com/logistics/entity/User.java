package com.logistics.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role; // admin, manager, employee, driver
    private String status; // active, inactive
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 