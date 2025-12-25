package com.logistics.common;

import lombok.Data;

/**
 * 分页请求参数
 */
@Data
public class PageRequest {
    private int pageNum = 1;    // 页码，从1开始
    private int pageSize = 10;  // 每页大小
    private String keyword;     // 搜索关键词
    private String status;      // 状态
    private String priority;    // 优先级
    private String city;        // 城市
    private String creditRating; // 信用等级
    
    // 司机管理搜索字段
    private String licenseType; // 驾照类型
    
    // 车辆管理搜索字段
    private String vehicleType; // 车辆类型
    private String brand;       // 品牌
    
    // 运输管理搜索字段
    private String transportType; // 运输类型
    private String transportStatus; // 运输状态
    
    // 费用管理搜索字段
    private String expenseType; // 费用类型
    private String approvalStatus; // 审批状态
    
    // 配送管理搜索字段
    private String deliveryStatus; // 配送状态
    
    // 仓库管理搜索字段
    private String warehouseType; // 仓库类型
    
    // 货物管理搜索字段
    private String goodsCategory; // 货物分类
    private String goodsStatus;   // 货物状态
    
    // 供应商管理搜索字段
    private String supplierType;  // 供应商类型
    
    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }
} 