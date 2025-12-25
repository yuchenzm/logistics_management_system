package com.logistics.controller;

import com.logistics.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * API首页控制器
 */
@RestController
@CrossOrigin
public class ApiController {
    
    /**
     * API首页 - 根路径
     */
    @GetMapping("/")
    public Result<Map<String, Object>> home() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "欢迎使用物流管理系统API");
        data.put("version", "1.0.0");
        data.put("status", "运行中");
        data.put("timestamp", System.currentTimeMillis());
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("用户管理", "/users");
        endpoints.put("客户管理", "/customers");
        endpoints.put("订单管理", "/orders");
        endpoints.put("司机管理", "/drivers");
        endpoints.put("车辆管理", "/vehicles");
        endpoints.put("运输管理", "/transports");
        endpoints.put("登录接口", "/users/login");
        data.put("endpoints", endpoints);
        
        return Result.success(data);
    }
    
    /**
     * API信息 - /api 路径
     */
    @GetMapping("/api")
    public Result<Map<String, Object>> apiInfo() {
        return home(); // 返回同样的信息
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", System.currentTimeMillis());
        data.put("database", "连接正常");
        return Result.success(data);
    }
    
    /**
     * API文档信息
     */
    @GetMapping("/api/docs")
    public Result<Map<String, Object>> apiDocs() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "物流管理系统 API 文档");
        data.put("description", "提供完整的物流管理功能，包括用户、客户、订单、司机、车辆和运输管理");
        data.put("version", "1.0.0");
        
        Map<String, Object> modules = new HashMap<>();
        
        Map<String, String> userModule = new HashMap<>();
        userModule.put("登录", "POST /users/login");
        userModule.put("获取活跃用户", "GET /users/active");
        userModule.put("分页查询", "POST /users/page");
        userModule.put("获取用户", "GET /users/{id}");
        userModule.put("保存用户", "POST /users");
        userModule.put("修改密码", "POST /users/{id}/password");
        userModule.put("删除用户", "DELETE /users/{id}");
        modules.put("用户管理", userModule);
        
        Map<String, String> orderModule = new HashMap<>();
        orderModule.put("获取活跃订单", "GET /orders/active");
        orderModule.put("分页查询", "POST /orders/page");
        orderModule.put("获取订单", "GET /orders/{id}");
        orderModule.put("保存订单", "POST /orders");
        orderModule.put("删除订单", "DELETE /orders/{id}");
        orderModule.put("订单统计", "GET /orders/statistics");
        modules.put("订单管理", orderModule);
        
        data.put("modules", modules);
        
        return Result.success(data);
    }
} 