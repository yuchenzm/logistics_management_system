package com.logistics.controller;

import com.logistics.entity.Driver;
import com.logistics.service.DriverService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 司机控制器
 */
@RestController
@RequestMapping("/drivers")
@CrossOrigin
public class DriverController {
    
    @Autowired
    private DriverService driverService;
    
    @GetMapping("/available")
    public Result<List<Driver>> getAvailable() {
        List<Driver> drivers = driverService.findAvailableDrivers();
        return Result.success(drivers);
    }
    
    @PostMapping("/page")
    public Result<PageResult<Driver>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Driver> result = driverService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result<Driver> getById(@PathVariable Long id) {
        Driver driver = driverService.findById(id);
        if (driver != null) {
            return Result.success(driver);
        } else {
            return Result.error("司机不存在");
        }
    }
    
    @PostMapping
    public Result<String> save(@RequestBody Driver driver) {
        boolean success = driverService.save(driver);
        if (success) {
            return Result.success(driver.getId() == null ? "司机创建成功" : "司机更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        String status = statusData.get("status");
        boolean success = driverService.updateStatus(id, status);
        if (success) {
            return Result.success("司机状态更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = driverService.delete(id);
        if (success) {
            return Result.success("司机删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 