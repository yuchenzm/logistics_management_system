package com.logistics.controller;

import com.logistics.entity.Inventory;
import com.logistics.service.InventoryService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存控制器
 */
@RestController
@RequestMapping("/inventory")
@CrossOrigin
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    /**
     * 获取所有库存
     */
    @GetMapping
    public Result<List<Inventory>> getAll() {
        List<Inventory> inventory = inventoryService.findAll();
        return Result.success(inventory);
    }
    
    /**
     * 分页查询库存
     */
    @PostMapping("/page")
    public Result<PageResult<Inventory>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Inventory> result = inventoryService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据仓库ID查询库存
     */
    @GetMapping("/warehouse/{warehouseId}")
    public Result<List<Inventory>> getByWarehouseId(@PathVariable Long warehouseId) {
        List<Inventory> inventory = inventoryService.findByWarehouseId(warehouseId);
        return Result.success(inventory);
    }
    
    /**
     * 根据货物ID查询库存
     */
    @GetMapping("/goods/{goodsId}")
    public Result<List<Inventory>> getByGoodsId(@PathVariable Long goodsId) {
        List<Inventory> inventory = inventoryService.findByGoodsId(goodsId);
        return Result.success(inventory);
    }
    
    /**
     * 根据关键词搜索库存
     */
    @GetMapping("/search")
    public Result<List<Inventory>> search(@RequestParam String keyword) {
        List<Inventory> inventory = inventoryService.searchByKeyword(keyword);
        return Result.success(inventory);
    }
    
    /**
     * 根据ID查询库存
     */
    @GetMapping("/{id}")
    public Result<Inventory> getById(@PathVariable Long id) {
        Inventory inventory = inventoryService.findById(id);
        if (inventory != null) {
            return Result.success(inventory);
        } else {
            return Result.error("库存记录不存在");
        }
    }
    
    /**
     * 保存库存（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody Inventory inventory) {
        boolean success = inventoryService.save(inventory);
        if (success) {
            return Result.success(inventory.getId() == null ? "库存记录创建成功" : "库存记录更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 库存入库
     */
    @PostMapping("/{id}/inbound")
    public Result<String> inbound(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id.intValue());
        boolean success = inventoryService.inbound(inventory);
        if (success) {
            return Result.success("入库成功");
        } else {
            return Result.error("入库失败");
        }
    }
    
    /**
     * 库存出库
     */
    @PostMapping("/{id}/outbound")
    public Result<String> outbound(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id.intValue());
        boolean success = inventoryService.outbound(inventory);
        if (success) {
            return Result.success("出库成功");
        } else {
            return Result.error("出库失败");
        }
    }
    
    /**
     * 库存调整
     */
    @PostMapping("/{id}/adjust")
    public Result<String> adjust(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id.intValue());
        boolean success = inventoryService.adjust(inventory);
        if (success) {
            return Result.success("库存调整成功");
        } else {
            return Result.error("库存调整失败");
        }
    }
    
    /**
     * 删除库存记录
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = inventoryService.delete(id);
        if (success) {
            return Result.success("库存记录删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 