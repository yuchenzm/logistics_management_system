package com.logistics.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderSummary {
    private Integer orderId;
    private String orderNumber;
    private String customerName;
    private String customerPhone;
    private String originCity;
    private String destinationCity;
    private BigDecimal totalWeight;
    private BigDecimal totalAmount;
    private String orderStatus; // pending, confirmed, picked_up, in_transit, delivered, cancelled
    private String paymentStatus; // pending, paid, partial, refunded
    private LocalDate expectedDeliveryDate;
    private LocalDateTime orderDate;
    private String createdByUser;

    // 构造函数
    public OrderSummary() {}

    public OrderSummary(String orderNumber, String customerName, String originCity, String destinationCity) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.originCity = originCity;
        this.destinationCity = destinationCity;
        this.orderStatus = "pending";
        this.paymentStatus = "pending";
        this.orderDate = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getOriginCity() { return originCity; }
    public void setOriginCity(String originCity) { this.originCity = originCity; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }

    public BigDecimal getTotalWeight() { return totalWeight; }
    public void setTotalWeight(BigDecimal totalWeight) { this.totalWeight = totalWeight; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public String getCreatedByUser() { return createdByUser; }
    public void setCreatedByUser(String createdByUser) { this.createdByUser = createdByUser; }
} 