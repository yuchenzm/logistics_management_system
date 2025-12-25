package com.logistics.entity;

import java.math.BigDecimal;

public class OrderDetail {
    private Integer id;
    private Integer orderId;
    private Integer goodsId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private BigDecimal weight;
    private BigDecimal volume;
    private String specialHandling;

    // 构造函数
    public OrderDetail() {}

    public OrderDetail(Integer orderId, Integer goodsId, Integer quantity, BigDecimal unitPrice) {
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // 业务方法
    public void calculateTotalPrice() {
        if (unitPrice != null && quantity != null) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public BigDecimal getTotalWeight() {
        if (weight != null && quantity != null) {
            return weight.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalVolume() {
        if (volume != null && quantity != null) {
            return volume.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getGoodsId() { return goodsId; }
    public void setGoodsId(Integer goodsId) { this.goodsId = goodsId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { 
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public BigDecimal getVolume() { return volume; }
    public void setVolume(BigDecimal volume) { this.volume = volume; }

    public String getSpecialHandling() { return specialHandling; }
    public void setSpecialHandling(String specialHandling) { this.specialHandling = specialHandling; }
} 