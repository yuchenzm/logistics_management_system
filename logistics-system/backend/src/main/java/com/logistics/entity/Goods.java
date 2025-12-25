package com.logistics.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Goods {
    private Integer id;
    private String name;
    private String sku;
    private String category;
    private String description;
    private BigDecimal weight;
    private BigDecimal volume;
    private BigDecimal unitPrice;
    private Boolean fragile;
    private Boolean hazardous;
    private String temperatureRequirements; // normal, frozen, refrigerated
    private Integer supplierId;
    private String status; // active, discontinued
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 构造函数
    public Goods() {}

    public Goods(String name, String category, BigDecimal weight, BigDecimal volume) {
        this.name = name;
        this.category = category;
        this.weight = weight;
        this.volume = volume;
        this.fragile = false;
        this.hazardous = false;
        this.temperatureRequirements = "normal";
        this.status = "active";
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public BigDecimal getVolume() { return volume; }
    public void setVolume(BigDecimal volume) { this.volume = volume; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Boolean getFragile() { return fragile; }
    public void setFragile(Boolean fragile) { this.fragile = fragile; }

    public Boolean getHazardous() { return hazardous; }
    public void setHazardous(Boolean hazardous) { this.hazardous = hazardous; }

    public String getTemperatureRequirements() { return temperatureRequirements; }
    public void setTemperatureRequirements(String temperatureRequirements) { this.temperatureRequirements = temperatureRequirements; }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 