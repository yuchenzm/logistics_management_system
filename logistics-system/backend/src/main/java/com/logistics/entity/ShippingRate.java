package com.logistics.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ShippingRate {
    private Integer id;
    private String originCity;
    private String destinationCity;
    private String transportType; // express, standard, economy
    private BigDecimal weightRangeMin;
    private BigDecimal weightRangeMax;
    private BigDecimal baseRate;
    private BigDecimal ratePerKg;
    private BigDecimal ratePerKm;
    private BigDecimal fuelSurchargeRate;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String status; // active, inactive
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 构造函数
    public ShippingRate() {}

    public ShippingRate(String originCity, String destinationCity, String transportType, BigDecimal baseRate) {
        this.originCity = originCity;
        this.destinationCity = destinationCity;
        this.transportType = transportType;
        this.baseRate = baseRate;
        this.status = "active";
        this.fuelSurchargeRate = BigDecimal.ZERO;
    }

    // 业务方法
    public BigDecimal calculateShippingCost(BigDecimal weight, BigDecimal distance) {
        BigDecimal cost = baseRate;
        
        if (ratePerKg != null && weight != null) {
            cost = cost.add(ratePerKg.multiply(weight));
        }
        
        if (ratePerKm != null && distance != null) {
            cost = cost.add(ratePerKm.multiply(distance));
        }
        
        if (fuelSurchargeRate != null) {
            cost = cost.add(cost.multiply(fuelSurchargeRate));
        }
        
        return cost;
    }

    public boolean isValidForWeight(BigDecimal weight) {
        if (weight == null) return true;
        
        boolean minValid = (weightRangeMin == null) || (weight.compareTo(weightRangeMin) >= 0);
        boolean maxValid = (weightRangeMax == null) || (weight.compareTo(weightRangeMax) <= 0);
        
        return minValid && maxValid;
    }

    public boolean isCurrentlyActive() {
        LocalDate now = LocalDate.now();
        boolean afterEffective = (effectiveDate == null) || (!now.isBefore(effectiveDate));
        boolean beforeExpiry = (expiryDate == null) || (!now.isAfter(expiryDate));
        
        return "active".equals(status) && afterEffective && beforeExpiry;
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getOriginCity() { return originCity; }
    public void setOriginCity(String originCity) { this.originCity = originCity; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }

    public String getTransportType() { return transportType; }
    public void setTransportType(String transportType) { this.transportType = transportType; }

    public BigDecimal getWeightRangeMin() { return weightRangeMin; }
    public void setWeightRangeMin(BigDecimal weightRangeMin) { this.weightRangeMin = weightRangeMin; }

    public BigDecimal getWeightRangeMax() { return weightRangeMax; }
    public void setWeightRangeMax(BigDecimal weightRangeMax) { this.weightRangeMax = weightRangeMax; }

    public BigDecimal getBaseRate() { return baseRate; }
    public void setBaseRate(BigDecimal baseRate) { this.baseRate = baseRate; }

    public BigDecimal getRatePerKg() { return ratePerKg; }
    public void setRatePerKg(BigDecimal ratePerKg) { this.ratePerKg = ratePerKg; }

    public BigDecimal getRatePerKm() { return ratePerKm; }
    public void setRatePerKm(BigDecimal ratePerKm) { this.ratePerKm = ratePerKm; }

    public BigDecimal getFuelSurchargeRate() { return fuelSurchargeRate; }
    public void setFuelSurchargeRate(BigDecimal fuelSurchargeRate) { this.fuelSurchargeRate = fuelSurchargeRate; }

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 