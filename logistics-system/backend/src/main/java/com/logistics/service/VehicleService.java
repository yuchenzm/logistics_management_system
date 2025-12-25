package com.logistics.service;

import com.logistics.entity.Vehicle;
import com.logistics.mapper.VehicleMapper;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 车辆服务类
 */
@Service
public class VehicleService {
    
    @Autowired
    private VehicleMapper vehicleMapper;
    
    public Vehicle findById(Long id) {
        return vehicleMapper.findById(id);
    }
    
    public Vehicle findByLicensePlate(String licensePlate) {
        return vehicleMapper.findByLicensePlate(licensePlate);
    }
    
    public List<Vehicle> findAvailableVehicles() {
        return vehicleMapper.findAvailableVehicles();
    }
    
    public List<Vehicle> findByStatus(String status) {
        return vehicleMapper.findByStatus(status);
    }
    
    public List<Vehicle> findByType(String vehicleType) {
        return vehicleMapper.findByType(vehicleType);
    }
    
    public List<Vehicle> findByDriverId(Long driverId) {
        return vehicleMapper.findByDriverId(driverId);
    }
    
    public List<Vehicle> searchByKeyword(String keyword) {
        return vehicleMapper.searchByKeyword(keyword);
    }
    
    public PageResult<Vehicle> findByPage(PageRequest pageRequest) {
        // 检查是否有搜索条件
        boolean hasSearchConditions = 
            StringUtils.hasText(pageRequest.getKeyword()) ||
            StringUtils.hasText(pageRequest.getVehicleType()) ||
            StringUtils.hasText(pageRequest.getBrand()) ||
            StringUtils.hasText(pageRequest.getStatus());
        
        if (hasSearchConditions) {
            // 使用多条件搜索
            List<Vehicle> vehicles = vehicleMapper.findByPageWithConditions(pageRequest);
            int total = vehicleMapper.countWithConditions(pageRequest);
            return PageResult.of(vehicles, total, pageRequest.getPageNum(), pageRequest.getPageSize());
        } else {
            // 使用普通分页查询
            List<Vehicle> vehicles = vehicleMapper.findByPage(pageRequest.getOffset(), pageRequest.getPageSize());
            int total = vehicleMapper.count();
            return PageResult.of(vehicles, total, pageRequest.getPageNum(), pageRequest.getPageSize());
        }
    }
    
    public boolean save(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            // 新增车辆
            if (vehicle.getStatus() == null) {
                vehicle.setStatus("available");
            }
            if (vehicle.getVehicleType() == null) {
                vehicle.setVehicleType("truck");
            }
            return vehicleMapper.insert(vehicle) > 0;
        } else {
            // 更新车辆
            return vehicleMapper.update(vehicle) > 0;
        }
    }
    
    public boolean updateStatus(Long id, String status) {
        Vehicle vehicle = vehicleMapper.findById(id);
        if (vehicle != null) {
            vehicle.setStatus(status);
            return vehicleMapper.update(vehicle) > 0;
        }
        return false;
    }
    
    public boolean assignDriver(Long vehicleId, Long driverId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle != null) {
            vehicle.setDriverId(driverId);
            return vehicleMapper.update(vehicle) > 0;
        }
        return false;
    }
    
    public boolean delete(Long id) {
        return vehicleMapper.delete(id) > 0;
    }
} 