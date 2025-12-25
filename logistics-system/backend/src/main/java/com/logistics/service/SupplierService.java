package com.logistics.service;

import com.logistics.entity.Supplier;
import com.logistics.mapper.SupplierMapper;
import com.logistics.common.PageRequest;
import com.logistics.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.math.BigDecimal;

@Service
public class SupplierService {
    
    @Autowired
    private SupplierMapper supplierMapper;
    
    public List<Supplier> findAll() {
        return supplierMapper.findAll();
    }
    
    /**
     * 查找所有活跃的供应商
     */
    public List<Supplier> findAllActive() {
        return supplierMapper.findByStatus("active");
    }
    
    /**
     * 分页查询供应商
     */
    public PageResult<Supplier> findByPage(PageRequest pageRequest) {
        List<Supplier> suppliers = supplierMapper.findAll();
        return new PageResult<>(suppliers, suppliers.size(), 1, suppliers.size());
    }
    
    public Supplier findById(Integer id) {
        return supplierMapper.findById(id);
    }
    
    public Supplier findById(Long id) {
        return supplierMapper.findById(id.intValue());
    }
    
    public List<Supplier> findBySupplierType(String supplierType) {
        return supplierMapper.findByServiceType(supplierType);
    }
    
    public List<Supplier> findByServiceType(String serviceType) {
        return supplierMapper.findByServiceType(serviceType);
    }
    
    public List<Supplier> findByCity(String city) {
        return supplierMapper.findByCity(city);
    }
    
    public List<Supplier> findByStatus(String status) {
        return supplierMapper.findByStatus(status);
    }
    
    public List<Supplier> findByCreditRating(String creditRating) {
        return supplierMapper.findByCreditRating(creditRating);
    }
    
    public List<Supplier> searchByKeyword(String keyword) {
        return supplierMapper.searchByKeyword(keyword);
    }
    
    public boolean save(Supplier supplier) {
        try {
            if (supplier.getId() == null) {
                // 新增
                if (supplier.getStatus() == null) {
                    supplier.setStatus("active");
                }
                if (supplier.getCreditRating() == null) {
                    supplier.setCreditRating("C");
                }
                supplierMapper.insert(supplier);
            } else {
                // 更新
                supplierMapper.update(supplier);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean updateStatus(Integer id, String status) {
        return supplierMapper.updateStatus(id, status) > 0;
    }
    
    /**
     * 更新供应商状态（通过Supplier对象）
     */
    public boolean updateStatus(Supplier supplier) {
        try {
            return supplierMapper.updateStatus(supplier.getId(), supplier.getStatus()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean updateCreditRating(Integer id, String creditRating) {
        return supplierMapper.updateCreditRating(id, creditRating) > 0;
    }
    
    public boolean deleteById(Integer id) {
        return supplierMapper.deleteById(id) > 0;
    }
    
    /**
     * 删除供应商（支持Long类型）
     */
    public boolean delete(Long id) {
        try {
            return supplierMapper.deleteById(id.intValue()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int count() {
        return supplierMapper.count();
    }
    
    public int countByStatus(String status) {
        return supplierMapper.countByStatus(status);
    }
    
    public int countByCreditRating(String creditRating) {
        return supplierMapper.countByCreditRating(creditRating);
    }
    
    public List<String> findAllCities() {
        return supplierMapper.findAllCities();
    }
    
    public List<String> findAllServiceTypes() {
        return supplierMapper.findAllServiceTypes();
    }
    
    public List<Supplier> findActiveSuppliers() {
        return supplierMapper.findByStatus("active");
    }
    
    public List<Supplier> findBlacklistedSuppliers() {
        return supplierMapper.findByStatus("blacklisted");
    }
    
    public List<Supplier> findTopRatedSuppliers() {
        return supplierMapper.findByCreditRating("A");
    }
} 