package com.logistics.controller;

import com.logistics.entity.Goods;
import com.logistics.service.GoodsService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 货物控制器
 */
@RestController
@RequestMapping("/goods")
@CrossOrigin
public class GoodsController {
    
    @Autowired
    private GoodsService goodsService;
    
    /**
     * 获取所有货物
     */
    @GetMapping
    public Result<List<Goods>> getAll() {
        List<Goods> goods = goodsService.findAll();
        return Result.success(goods);
    }
    
    /**
     * 分页查询货物
     */
    @PostMapping("/page")
    public Result<PageResult<Goods>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Goods> result = goodsService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据分类查询货物
     */
    @GetMapping("/category/{category}")
    public Result<List<Goods>> getByCategory(@PathVariable String category) {
        List<Goods> goods = goodsService.findByCategory(category);
        return Result.success(goods);
    }
    
    /**
     * 根据状态查询货物
     */
    @GetMapping("/status/{status}")
    public Result<List<Goods>> getByStatus(@PathVariable String status) {
        List<Goods> goods = goodsService.findByStatus(status);
        return Result.success(goods);
    }
    
    /**
     * 根据关键词搜索货物
     */
    @GetMapping("/search")
    public Result<List<Goods>> search(@RequestParam String keyword) {
        List<Goods> goods = goodsService.searchByKeyword(keyword);
        return Result.success(goods);
    }
    
    /**
     * 根据SKU查询货物
     */
    @GetMapping("/sku/{sku}")
    public Result<Goods> getBySku(@PathVariable String sku) {
        Goods goods = goodsService.findBySku(sku);
        if (goods != null) {
            return Result.success(goods);
        } else {
            return Result.error("货物不存在");
        }
    }
    
    /**
     * 根据ID查询货物
     */
    @GetMapping("/{id}")
    public Result<Goods> getById(@PathVariable Long id) {
        Goods goods = goodsService.findById(id);
        if (goods != null) {
            return Result.success(goods);
        } else {
            return Result.error("货物不存在");
        }
    }
    
    /**
     * 保存货物（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody Goods goods) {
        boolean success = goodsService.save(goods);
        if (success) {
            return Result.success(goods.getId() == null ? "货物创建成功" : "货物更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 更新货物状态
     */
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Goods goods) {
        goods.setId(id.intValue());
        boolean success = goodsService.updateStatus(goods);
        if (success) {
            return Result.success("货物状态更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 删除货物
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = goodsService.delete(id);
        if (success) {
            return Result.success("货物删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 