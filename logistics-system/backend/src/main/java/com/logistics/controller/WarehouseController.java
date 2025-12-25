package com.logistics.controller;

import com.logistics.entity.Warehouse;
import com.logistics.service.WarehouseService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 仓库控制器
 */
@RestController
@RequestMapping("/warehouses")
@CrossOrigin
public class WarehouseController {
    
    @Autowired
    private WarehouseService warehouseService;
    
    /**
     * 获取所有仓库
     */
    @GetMapping
    public Result<List<Warehouse>> getAll() {
        List<Warehouse> warehouses = warehouseService.findAll();
        return Result.success(warehouses);
    }
    
    /**
     * 分页查询仓库
     */
    @PostMapping("/page")
    public Result<PageResult<Warehouse>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Warehouse> result = warehouseService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据类型查询仓库
     */
    @GetMapping("/type/{type}")
    public Result<List<Warehouse>> getByType(@PathVariable String type) {
        List<Warehouse> warehouses = warehouseService.findByType(type);
        return Result.success(warehouses);
    }
    
    /**
     * 根据城市查询仓库
     */
    @GetMapping("/city/{city}")
    public Result<List<Warehouse>> getByCity(@PathVariable String city) {
        List<Warehouse> warehouses = warehouseService.findByCity(city);
        return Result.success(warehouses);
    }
    
    /**
     * 根据关键词搜索仓库
     */
    @GetMapping("/search")
    public Result<List<Warehouse>> search(@RequestParam String keyword) {
        List<Warehouse> warehouses = warehouseService.searchByKeyword(keyword);
        return Result.success(warehouses);
    }
    
    /**
     * 根据ID查询仓库
     */
    @GetMapping("/{id}")
    public Result<Warehouse> getById(@PathVariable Long id) {
        Warehouse warehouse = warehouseService.findById(id);
        if (warehouse != null) {
            return Result.success(warehouse);
        } else {
            return Result.error("仓库不存在");
        }
    }
    
    /**
     * 保存仓库（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody Warehouse warehouse) {
        boolean success = warehouseService.save(warehouse);
        if (success) {
            return Result.success(warehouse.getId() == null ? "仓库创建成功" : "仓库更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 更新仓库状态
     */
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Warehouse warehouse) {
        warehouse.setId(id);
        boolean success = warehouseService.updateStatus(warehouse);
        if (success) {
            return Result.success("仓库状态更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 删除仓库
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = warehouseService.delete(id);
        if (success) {
            return Result.success("仓库删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 