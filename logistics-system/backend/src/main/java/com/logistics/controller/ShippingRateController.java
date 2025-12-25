package com.logistics.controller;

import com.logistics.entity.ShippingRate;
import com.logistics.service.ShippingRateService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运费控制器
 */
@RestController
@RequestMapping("/shipping-rates")
@CrossOrigin
public class ShippingRateController {
    
    @Autowired
    private ShippingRateService shippingRateService;
    
    /**
     * 获取所有运费
     */
    @GetMapping
    public Result<List<ShippingRate>> getAll() {
        List<ShippingRate> rates = shippingRateService.findAll();
        return Result.success(rates);
    }
    
    /**
     * 分页查询运费
     */
    @PostMapping("/page")
    public Result<PageResult<ShippingRate>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<ShippingRate> result = shippingRateService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据起始城市查询运费
     */
    @GetMapping("/origin/{originCity}")
    public Result<List<ShippingRate>> getByOriginCity(@PathVariable String originCity) {
        List<ShippingRate> rates = shippingRateService.findByOriginCity(originCity);
        return Result.success(rates);
    }
    
    /**
     * 根据目的城市查询运费
     */
    @GetMapping("/destination/{destinationCity}")
    public Result<List<ShippingRate>> getByDestinationCity(@PathVariable String destinationCity) {
        List<ShippingRate> rates = shippingRateService.findByDestinationCity(destinationCity);
        return Result.success(rates);
    }
    
    /**
     * 根据运输类型查询运费
     */
    @GetMapping("/type/{transportType}")
    public Result<List<ShippingRate>> getByTransportType(@PathVariable String transportType) {
        List<ShippingRate> rates = shippingRateService.findByTransportType(transportType);
        return Result.success(rates);
    }
    
    /**
     * 查询路线运费
     */
    @GetMapping("/route")
    public Result<List<ShippingRate>> getByRoute(@RequestParam String originCity, @RequestParam String destinationCity) {
        List<ShippingRate> rates = shippingRateService.findByRoute(originCity, destinationCity);
        return Result.success(rates);
    }
    
    /**
     * 计算运费
     */
    @PostMapping("/calculate")
    public Result<Double> calculateShippingCost(@RequestBody ShippingRate calculateRequest) {
        Double cost = shippingRateService.calculateShippingCost(calculateRequest);
        return Result.success(cost);
    }
    
    /**
     * 根据关键词搜索运费
     */
    @GetMapping("/search")
    public Result<List<ShippingRate>> search(@RequestParam String keyword) {
        List<ShippingRate> rates = shippingRateService.searchByKeyword(keyword);
        return Result.success(rates);
    }
    
    /**
     * 根据ID查询运费
     */
    @GetMapping("/{id}")
    public Result<ShippingRate> getById(@PathVariable Long id) {
        ShippingRate rate = shippingRateService.findById(id);
        if (rate != null) {
            return Result.success(rate);
        } else {
            return Result.error("运费记录不存在");
        }
    }
    
    /**
     * 保存运费（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody ShippingRate rate) {
        boolean success = shippingRateService.save(rate);
        if (success) {
            return Result.success(rate.getId() == null ? "运费创建成功" : "运费更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 批量更新运费
     */
    @PostMapping("/batch-update")
    public Result<String> batchUpdate(@RequestBody List<ShippingRate> rates) {
        boolean success = shippingRateService.batchUpdate(rates);
        if (success) {
            return Result.success("批量更新成功");
        } else {
            return Result.error("批量更新失败");
        }
    }
    
    /**
     * 删除运费
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = shippingRateService.delete(id);
        if (success) {
            return Result.success("运费删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 