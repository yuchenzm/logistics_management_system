package com.logistics.controller;

import com.logistics.entity.Supplier;
import com.logistics.service.SupplierService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商控制器
 */
@RestController
@RequestMapping("/suppliers")
@CrossOrigin
public class SupplierController {
    
    @Autowired
    private SupplierService supplierService;
    
    /**
     * 获取所有活跃供应商
     */
    @GetMapping("/active")
    public Result<List<Supplier>> getAllActive() {
        List<Supplier> suppliers = supplierService.findAllActive();
        return Result.success(suppliers);
    }
    
    /**
     * 分页查询供应商
     */
    @PostMapping("/page")
    public Result<PageResult<Supplier>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Supplier> result = supplierService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据状态查询供应商
     */
    @GetMapping("/status/{status}")
    public Result<List<Supplier>> getByStatus(@PathVariable String status) {
        List<Supplier> suppliers = supplierService.findByStatus(status);
        return Result.success(suppliers);
    }
    
    /**
     * 根据城市查询供应商
     */
    @GetMapping("/city/{city}")
    public Result<List<Supplier>> getByCity(@PathVariable String city) {
        List<Supplier> suppliers = supplierService.findByCity(city);
        return Result.success(suppliers);
    }
    
    /**
     * 根据关键词搜索供应商
     */
    @GetMapping("/search")
    public Result<List<Supplier>> search(@RequestParam String keyword) {
        List<Supplier> suppliers = supplierService.searchByKeyword(keyword);
        return Result.success(suppliers);
    }
    
    /**
     * 根据ID查询供应商
     */
    @GetMapping("/{id}")
    public Result<Supplier> getById(@PathVariable Long id) {
        Supplier supplier = supplierService.findById(id);
        if (supplier != null) {
            return Result.success(supplier);
        } else {
            return Result.error("供应商不存在");
        }
    }
    
    /**
     * 保存供应商（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody Supplier supplier) {
        boolean success = supplierService.save(supplier);
        if (success) {
            return Result.success(supplier.getId() == null ? "供应商创建成功" : "供应商更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 更新供应商状态
     */
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Supplier supplier) {
        supplier.setId(id.intValue());
        boolean success = supplierService.updateStatus(supplier);
        if (success) {
            return Result.success("供应商状态更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 删除供应商
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = supplierService.delete(id);
        if (success) {
            return Result.success("供应商删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 