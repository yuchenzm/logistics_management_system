package com.logistics.service;

import com.logistics.entity.Driver;
import com.logistics.mapper.DriverMapper;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 司机服务类
 */
@Service
public class DriverService {
    
    @Autowired
    private DriverMapper driverMapper;
    
    public Driver findById(Long id) {
        return driverMapper.findById(id);
    }
    
    public Driver findByLicenseNumber(String licenseNumber) {
        return driverMapper.findByLicenseNumber(licenseNumber);
    }
    
    public List<Driver> findAvailableDrivers() {
        return driverMapper.findAvailableDrivers();
    }
    
    public List<Driver> findByStatus(String status) {
        return driverMapper.findByStatus(status);
    }
    
    public List<Driver> searchByKeyword(String keyword) {
        return driverMapper.searchByKeyword(keyword);
    }
    
    public PageResult<Driver> findByPage(PageRequest pageRequest) {
        // 检查是否有搜索条件
        boolean hasSearchConditions = 
            StringUtils.hasText(pageRequest.getKeyword()) ||
            StringUtils.hasText(pageRequest.getStatus()) ||
            StringUtils.hasText(pageRequest.getLicenseType());
        
        if (hasSearchConditions) {
            // 使用多条件搜索
            List<Driver> drivers = driverMapper.findByPageWithConditions(pageRequest);
            int total = driverMapper.countWithConditions(pageRequest);
            return PageResult.of(drivers, total, pageRequest.getPageNum(), pageRequest.getPageSize());
        } else {
            // 使用普通分页查询
            List<Driver> drivers = driverMapper.findByPage(pageRequest.getOffset(), pageRequest.getPageSize());
            int total = driverMapper.count();
            return PageResult.of(drivers, total, pageRequest.getPageNum(), pageRequest.getPageSize());
        }
    }
    
    public boolean save(Driver driver) {
        if (driver.getId() == null) {
            // 新增司机
            if (driver.getStatus() == null) {
                driver.setStatus("available");
            }
            if (driver.getLicenseType() == null) {
                driver.setLicenseType("B2");
            }
            if (driver.getRating() == null) {
                driver.setRating(java.math.BigDecimal.ZERO);
            }
            return driverMapper.insert(driver) > 0;
        } else {
            // 更新司机
            return driverMapper.update(driver) > 0;
        }
    }
    
    public boolean updateStatus(Long id, String status) {
        return driverMapper.updateStatus(id, status) > 0;
    }
    
    public boolean delete(Long id) {
        return driverMapper.delete(id) > 0;
    }
} 