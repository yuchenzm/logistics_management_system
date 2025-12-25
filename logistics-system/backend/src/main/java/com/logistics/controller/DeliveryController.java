package com.logistics.controller;

import com.logistics.entity.Delivery;
import com.logistics.service.DeliveryService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配送控制器
 */
@RestController
@RequestMapping("/deliveries")
@CrossOrigin
public class DeliveryController {
    
    @Autowired
    private DeliveryService deliveryService;
    
    /**
     * 获取所有配送
     */
    @GetMapping
    public Result<List<Delivery>> getAll() {
        List<Delivery> deliveries = deliveryService.findAll();
        return Result.success(deliveries);
    }
    
    /**
     * 分页查询配送
     */
    @PostMapping("/page")
    public Result<PageResult<Delivery>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Delivery> result = deliveryService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据状态查询配送
     */
    @GetMapping("/status/{status}")
    public Result<List<Delivery>> getByStatus(@PathVariable String status) {
        List<Delivery> deliveries = deliveryService.findByStatus(status);
        return Result.success(deliveries);
    }
    
    /**
     * 根据运输ID查询配送
     */
    @GetMapping("/transport/{transportId}")
    public Result<List<Delivery>> getByTransportId(@PathVariable Long transportId) {
        List<Delivery> deliveries = deliveryService.findByTransportId(transportId);
        return Result.success(deliveries);
    }
    
    /**
     * 根据配送员查询配送
     */
    @GetMapping("/deliverer/{deliverer}")
    public Result<List<Delivery>> getByDeliverer(@PathVariable String deliverer) {
        List<Delivery> deliveries = deliveryService.findByDeliverer(deliverer);
        return Result.success(deliveries);
    }
    
    /**
     * 根据关键词搜索配送
     */
    @GetMapping("/search")
    public Result<List<Delivery>> search(@RequestParam String keyword) {
        List<Delivery> deliveries = deliveryService.searchByKeyword(keyword);
        return Result.success(deliveries);
    }
    
    /**
     * 根据运输单号查询配送
     */
    @GetMapping("/transport-number/{transportNumber}")
    public Result<List<Delivery>> getByTransportNumber(@PathVariable String transportNumber) {
        List<Delivery> deliveries = deliveryService.findByTransportNumber(transportNumber);
        return Result.success(deliveries);
    }
    
    /**
     * 根据ID查询配送
     */
    @GetMapping("/{id}")
    public Result<Delivery> getById(@PathVariable Long id) {
        Delivery delivery = deliveryService.findById(id);
        if (delivery != null) {
            return Result.success(delivery);
        } else {
            return Result.error("配送记录不存在");
        }
    }
    
    /**
     * 保存配送（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody Delivery delivery) {
        boolean success = deliveryService.save(delivery);
        if (success) {
            return Result.success(delivery.getId() == null ? "配送记录创建成功" : "配送记录更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 更新配送状态
     */
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Delivery delivery) {
        delivery.setId(id.intValue());
        boolean success = deliveryService.updateStatus(delivery);
        if (success) {
            return Result.success("配送状态更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 配送签收
     */
    @PostMapping("/{id}/sign")
    public Result<String> signForDelivery(@PathVariable Long id, @RequestBody Delivery delivery) {
        delivery.setId(id.intValue());
        boolean success = deliveryService.signForDelivery(delivery);
        if (success) {
            return Result.success("签收成功");
        } else {
            return Result.error("签收失败");
        }
    }
    
    /**
     * 删除配送记录
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = deliveryService.delete(id);
        if (success) {
            return Result.success("配送记录删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 