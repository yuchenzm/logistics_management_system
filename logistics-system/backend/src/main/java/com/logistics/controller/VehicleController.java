package com.logistics.controller;

import com.logistics.entity.Vehicle;
import com.logistics.service.VehicleService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 车辆控制器
 */
@RestController
@RequestMapping("/vehicles")
@CrossOrigin
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @GetMapping("/available")
    public Result<List<Vehicle>> getAvailable() {
        List<Vehicle> vehicles = vehicleService.findAvailableVehicles();
        return Result.success(vehicles);
    }
    
    @GetMapping("/type/{vehicleType}")
    public Result<List<Vehicle>> getByType(@PathVariable String vehicleType) {
        List<Vehicle> vehicles = vehicleService.findByType(vehicleType);
        return Result.success(vehicles);
    }
    
    @PostMapping("/page")
    public Result<PageResult<Vehicle>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Vehicle> result = vehicleService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result<Vehicle> getById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.findById(id);
        if (vehicle != null) {
            return Result.success(vehicle);
        } else {
            return Result.error("车辆不存在");
        }
    }
    
    @PostMapping
    public Result<String> save(@RequestBody Vehicle vehicle) {
        boolean success = vehicleService.save(vehicle);
        if (success) {
            return Result.success(vehicle.getId() == null ? "车辆创建成功" : "车辆更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        String status = statusData.get("status");
        boolean success = vehicleService.updateStatus(id, status);
        if (success) {
            return Result.success("车辆状态更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    @PutMapping("/{id}/driver")
    public Result<String> assignDriver(@PathVariable Long id, @RequestBody Map<String, Long> driverData) {
        Long driverId = driverData.get("driverId");
        boolean success = vehicleService.assignDriver(id, driverId);
        if (success) {
            return Result.success("司机分配成功");
        } else {
            return Result.error("分配失败");
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = vehicleService.delete(id);
        if (success) {
            return Result.success("车辆删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 