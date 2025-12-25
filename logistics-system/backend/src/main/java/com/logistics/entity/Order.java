package com.logistics.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
public class Order {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private String originAddress;
    private String originCity;
    private String originProvince;
    private String destinationAddress;
    private String destinationCity;
    private String destinationProvince;
    private LocalDate pickupDate;
    private LocalDate deliveryDate;
    private LocalDate expectedDeliveryDate;
    private BigDecimal totalWeight;
    private BigDecimal totalVolume;
    private BigDecimal totalAmount;
    private String paymentStatus; // pending, paid, partial, refunded
    private String orderStatus; // pending, confirmed, picked_up, in_transit, delivered, cancelled
    private String priority; // low, normal, high, urgent
    private String remarks; // 之前是 specialInstructions
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 关联信息
    private String customerName;
    private String customerPhone;
    
    // 前端兼容的旧字段已移除
} 