package com.logistics.service;

import com.logistics.entity.Warehouse;
import com.logistics.mapper.WarehouseMapper;
import com.logistics.common.PageRequest;
import com.logistics.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WarehouseService {
    
    @Autowired
    private WarehouseMapper warehouseMapper;
    
    public List<Warehouse> findAll() {
        return warehouseMapper.findAll();
    }
    
    /**
     * 分页查询仓库
     */
    public PageResult<Warehouse> findByPage(PageRequest pageRequest) {
        // 检查是否有搜索条件
        boolean hasConditions = (pageRequest.getKeyword() != null && !pageRequest.getKeyword().trim().isEmpty()) ||
                              (pageRequest.getWarehouseType() != null && !pageRequest.getWarehouseType().trim().isEmpty()) ||
                              (pageRequest.getStatus() != null && !pageRequest.getStatus().trim().isEmpty());
        
        if (hasConditions) {
            // 有搜索条件，使用多条件查询
            List<Warehouse> warehouses = warehouseMapper.findByPageWithConditions(
                pageRequest.getKeyword(),
                pageRequest.getWarehouseType(),
                pageRequest.getStatus(),
                pageRequest.getOffset(),
                pageRequest.getPageSize()
            );
            
            int total = warehouseMapper.countWithConditions(
                pageRequest.getKeyword(),
                pageRequest.getWarehouseType(),
                pageRequest.getStatus()
            );
            
            int totalPages = (int) Math.ceil((double) total / pageRequest.getPageSize());
            return new PageResult<>(warehouses, total, pageRequest.getPageNum(), totalPages);
        } else {
            // 无搜索条件，查询所有数据
            List<Warehouse> allWarehouses = warehouseMapper.findAll();
            int total = allWarehouses.size();
            int totalPages = (int) Math.ceil((double) total / pageRequest.getPageSize());
            
            // 手动分页
            int start = pageRequest.getOffset();
            int end = Math.min(start + pageRequest.getPageSize(), total);
            List<Warehouse> warehouses = allWarehouses.subList(start, end);
            
            return new PageResult<>(warehouses, total, pageRequest.getPageNum(), totalPages);
        }
    }
    
    public Warehouse findById(Integer id) {
        return warehouseMapper.findById(id);
    }
    
    public Warehouse findById(Long id) {
        return warehouseMapper.findById(id.intValue());
    }
    
    public Warehouse findByCode(String code) {
        return warehouseMapper.findByCode(code);
    }
    
    public List<Warehouse> findByCity(String city) {
        return warehouseMapper.findByCity(city);
    }
    
    public List<Warehouse> findByType(String warehouseType) {
        return warehouseMapper.findByType(warehouseType);
    }
    
    public List<Warehouse> findByStatus(String status) {
        return warehouseMapper.findByStatus(status);
    }
    
    public List<Warehouse> searchByKeyword(String keyword) {
        return warehouseMapper.searchByKeyword(keyword);
    }
    
    public boolean save(Warehouse warehouse) {
        try {
            if (warehouse.getId() == null) {
                // 新增
                if (warehouse.getStatus() == null) {
                    warehouse.setStatus("active");
                }
                warehouseMapper.insert(warehouse);
            } else {
                // 更新
                warehouseMapper.update(warehouse);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean updateStatus(Integer id, String status) {
        return warehouseMapper.updateStatus(id, status) > 0;
    }
    
    /**
     * 更新仓库状态（通过Warehouse对象）
     */
    public boolean updateStatus(Warehouse warehouse) {
        try {
            return warehouseMapper.updateStatus(warehouse.getId(), warehouse.getStatus()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean deleteById(Integer id) {
        return warehouseMapper.deleteById(id) > 0;
    }
    
    /**
     * 删除仓库（支持Long类型）
     */
    public boolean delete(Long id) {
        try {
            return warehouseMapper.deleteById(id.intValue()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int count() {
        return warehouseMapper.count();
    }
    
    public int countByStatus(String status) {
        return warehouseMapper.countByStatus(status);
    }
    
    public List<String> findAllCities() {
        return warehouseMapper.findAllCities();
    }
    
    public List<String> findAllTypes() {
        return warehouseMapper.findAllTypes();
    }
    
    public List<Warehouse> findActiveWarehouses() {
        return warehouseMapper.findByStatus("active");
    }
} 