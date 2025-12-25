package com.logistics.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 运输实体类
 */
@Data
public class Transport {
    private Long id;
    private String transportNumber;
    private Long orderId;
    private Long vehicleId;
    private Long driverId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer estimatedDuration; // 估计运输时间（小时）
    private Integer actualDuration; // 实际运输时间（小时）
    private BigDecimal distance; // 运输距离（公里）
    private BigDecimal fuelCost;
    private BigDecimal tollCost;
    private BigDecimal otherCosts;
    private BigDecimal totalCost;
    private String transportStatus; // planned, in_progress, completed, cancelled, delayed
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联信息
    private String orderNumber;
    private String driverName;
    private String vehicleLicensePlate;
    private String originCity;
    private String destinationCity;
} 