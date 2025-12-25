package com.logistics.controller;

import com.logistics.entity.Customer;
import com.logistics.service.CustomerService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户控制器
 */
@RestController
@RequestMapping("/customers")
@CrossOrigin
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * 获取所有活跃客户
     */
    @GetMapping("/active")
    public Result<List<Customer>> getAllActive() {
        List<Customer> customers = customerService.findAllActive();
        return Result.success(customers);
    }
    
    /**
     * 分页查询客户
     */
    @PostMapping("/page")
    public Result<PageResult<Customer>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Customer> result = customerService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据关键词搜索客户
     */
    @GetMapping("/search")
    public Result<List<Customer>> search(@RequestParam String keyword) {
        List<Customer> customers = customerService.searchByKeyword(keyword);
        return Result.success(customers);
    }
    
    /**
     * 根据ID查询客户
     */
    @GetMapping("/{id}")
    public Result<Customer> getById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            return Result.success(customer);
        } else {
            return Result.error("客户不存在");
        }
    }
    
    /**
     * 保存客户（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody Customer customer) {
        boolean success = customerService.save(customer);
        if (success) {
            return Result.success(customer.getId() == null ? "客户创建成功" : "客户更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 删除客户
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = customerService.delete(id);
        if (success) {
            return Result.success("客户删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 