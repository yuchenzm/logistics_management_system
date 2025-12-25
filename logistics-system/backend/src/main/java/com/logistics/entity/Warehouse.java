package com.logistics.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Warehouse {
    private Integer id;
    private String name;
    private String code;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private BigDecimal capacity;
    private Integer managerId;
    private String warehouseType; // storage, distribution, cold_storage, hazardous
    private String status; // active, inactive, maintenance
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 构造函数
    public Warehouse() {}

    public Warehouse(String name, String code, String city, String warehouseType) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.warehouseType = warehouseType;
        this.status = "active";
    }

    // 业务方法
    public boolean isActive() {
        return "active".equals(this.status);
    }

    public boolean canStoreHazardous() {
        return "hazardous".equals(this.warehouseType);
    }

    public boolean canStoreFrozen() {
        return "cold_storage".equals(this.warehouseType);
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (address != null) sb.append(address);
        if (city != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }
        if (province != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(province);
        }
        if (postalCode != null) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(postalCode);
        }
        return sb.toString();
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Object id) { 
        if (id instanceof Long) {
            this.id = ((Long) id).intValue();
        } else if (id instanceof Integer) {
            this.id = (Integer) id;
        } else if (id != null) {
            this.id = Integer.valueOf(id.toString());
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public BigDecimal getCapacity() { return capacity; }
    public void setCapacity(BigDecimal capacity) { this.capacity = capacity; }

    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }

    public String getWarehouseType() { return warehouseType; }
    public void setWarehouseType(String warehouseType) { this.warehouseType = warehouseType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 