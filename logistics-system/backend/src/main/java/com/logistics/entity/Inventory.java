package com.logistics.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Inventory {
    private Integer id;
    private Integer warehouseId;
    private Integer goodsId;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer safetyStock;
    private String location;
    private String batchNumber;
    private LocalDate expiryDate;
    private LocalDateTime lastUpdated;

    // 构造函数
    public Inventory() {}

    public Inventory(Integer warehouseId, Integer goodsId, Integer quantity) {
        this.warehouseId = warehouseId;
        this.goodsId = goodsId;
        this.quantity = quantity;
        this.reservedQuantity = 0;
        this.safetyStock = 10;
    }

    // 业务方法
    public Integer getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public void addStock(Integer amount) {
        this.quantity += amount;
    }

    public void removeStock(Integer amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }

    public void reserveStock(Integer amount) {
        int available = getAvailableQuantity();
        this.reservedQuantity += Math.min(amount, available);
    }

    public void releaseReservedStock(Integer amount) {
        this.reservedQuantity = Math.max(0, this.reservedQuantity - amount);
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Integer warehouseId) { this.warehouseId = warehouseId; }

    public Integer getGoodsId() { return goodsId; }
    public void setGoodsId(Integer goodsId) { this.goodsId = goodsId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getActualQuantity() { return quantity; }

    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public Integer getSafetyStock() { return safetyStock; }
    public void setSafetyStock(Integer safetyStock) { this.safetyStock = safetyStock; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
} 