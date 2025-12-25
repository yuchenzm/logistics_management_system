package com.logistics.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车辆实体类
 */
@Data
public class Vehicle {
    private Long id;
    private String licensePlate;
    private String brand;
    private String model;
    private String vehicleType; // truck, van, pickup, container
    private BigDecimal capacityWeight;
    private BigDecimal capacityVolume;
    private String fuelType; // gasoline, diesel, electric, hybrid
    private Integer yearManufactured;
    private Long driverId;
    private String status; // available, in_use, maintenance, inactive
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联信息
    private String driverName;
} 