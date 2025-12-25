package com.logistics.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Supplier {
    private Integer id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String serviceType; // transport, warehouse, packaging, insurance
    private BigDecimal rating;
    private String status; // active, inactive
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 构造函数
    public Supplier() {}

    public Supplier(String name, String contactPerson, String serviceType) {
        this.name = name;
        this.contactPerson = contactPerson;
        this.serviceType = serviceType;
        this.rating = BigDecimal.ZERO;
        this.status = "active";
    }

    // 业务方法
    public void updateRating(BigDecimal newRating) {
        if (newRating.compareTo(BigDecimal.ZERO) >= 0 && newRating.compareTo(BigDecimal.valueOf(5)) <= 0) {
            this.rating = newRating;
        }
    }

    public boolean isActive() {
        return "active".equals(this.status);
    }

    public String getDisplayName() {
        return name + (contactPerson != null ? " (" + contactPerson + ")" : "");
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    // 为Service层兼容性添加的别名方法
    public String getCreditRating() {
        if (rating == null) return "C";
        if (rating.compareTo(BigDecimal.valueOf(4.5)) >= 0) return "A";
        if (rating.compareTo(BigDecimal.valueOf(3.5)) >= 0) return "B";
        if (rating.compareTo(BigDecimal.valueOf(2.5)) >= 0) return "C";
        return "D";
    }
    
    public void setCreditRating(String creditRating) {
        switch (creditRating) {
            case "A": this.rating = BigDecimal.valueOf(5.0); break;
            case "B": this.rating = BigDecimal.valueOf(4.0); break;
            case "C": this.rating = BigDecimal.valueOf(3.0); break;
            case "D": this.rating = BigDecimal.valueOf(2.0); break;
            default: this.rating = BigDecimal.valueOf(3.0); break;
        }
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 