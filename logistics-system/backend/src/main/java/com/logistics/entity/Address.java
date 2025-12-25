package com.logistics.entity;

import java.time.LocalDateTime;

/**
 * 地址实体类
 */
public class Address {
    private Long id;
    private Long userId;           // 用户ID
    private String contactName;    // 联系人姓名
    private String contactPhone;   // 联系人电话
    private String province;       // 省份
    private String city;          // 城市
    private String district;      // 区域
    private String address;       // 详细地址
    private Boolean isDefault;    // 是否默认地址
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    // 构造函数
    public Address() {}

    public Address(Long userId, String contactName, String contactPhone, 
                   String province, String city, String district, 
                   String address, Boolean isDefault) {
        this.userId = userId;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.isDefault = isDefault;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", userId=" + userId +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", isDefault=" + isDefault +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 