package com.logistics.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransportSummary {
    private Integer transportId;
    private String transportNumber;
    private String orderNumber;
    private String customerName;
    private String driverName;
    private String licensePlate;
    private String vehicleBrand;
    private String transportStatus; // planned, in_progress, completed, cancelled, delayed
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal distance;
    private BigDecimal totalCost;
    private String originCity;
    private String destinationCity;

    // 构造函数
    public TransportSummary() {}

    public TransportSummary(String transportNumber, String orderNumber, String transportStatus) {
        this.transportNumber = transportNumber;
        this.orderNumber = orderNumber;
        this.transportStatus = transportStatus;
    }

    // 业务方法
    public Long getTransportDuration() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).toMinutes();
        }
        return null;
    }

    public boolean isCompleted() {
        return "completed".equals(transportStatus);
    }

    public boolean isInProgress() {
        return "in_progress".equals(transportStatus);
    }

    // Getter和Setter方法
    public Integer getTransportId() { return transportId; }
    public void setTransportId(Integer transportId) { this.transportId = transportId; }

    public String getTransportNumber() { return transportNumber; }
    public void setTransportNumber(String transportNumber) { this.transportNumber = transportNumber; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getVehicleBrand() { return vehicleBrand; }
    public void setVehicleBrand(String vehicleBrand) { this.vehicleBrand = vehicleBrand; }

    public String getTransportStatus() { return transportStatus; }
    public void setTransportStatus(String transportStatus) { this.transportStatus = transportStatus; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BigDecimal getDistance() { return distance; }
    public void setDistance(BigDecimal distance) { this.distance = distance; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public String getOriginCity() { return originCity; }
    public void setOriginCity(String originCity) { this.originCity = originCity; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }
} 