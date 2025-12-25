/*
 * @Author: yuchenzm Wcm136677@163.com
 * @Date: 2025-07-06 02:02:36
 * @LastEditors: yuchenzm Wcm136677@163.com
 * @LastEditTime: 2025-07-06 16:06:30
 * @FilePath: /wuliu guanli/logistics-system/backend/src/main/java/com/logistics/controller/AddressController.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
package com.logistics.controller;

import com.logistics.common.JwtUtil;
import com.logistics.entity.Address;
import com.logistics.service.AddressService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址控制器
 */
@RestController
@RequestMapping("/addresses")
@CrossOrigin
public class AddressController {
    
    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 根据用户ID获取地址列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Address>> getByUserId(@PathVariable Long userId) {
        List<Address> addresses = addressService.findByUserId(userId);
        return Result.success(addresses);
    }
    
    /**
     * 根据ID获取地址
     */
    @GetMapping("/{id}")
    public Result<Address> getById(@PathVariable Long id) {
        Address address = addressService.findById(id);
        if (address != null) {
            return Result.success(address);
        } else {
            return Result.error("地址不存在");
        }
    }
    
    /**
     * 创建地址
     */
    @PostMapping
    public Result<Address> create(@RequestBody Address address, @RequestHeader("Authorization") String token) {
        // 从token中解析用户ID
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        address.setUserId(userId);

        Address savedAddress = addressService.save(address);
        if (savedAddress != null) {
            return Result.success("地址创建成功", savedAddress);
        } else {
            return Result.error("地址创建失败");
        }
    }
    
    /**
     * 更新地址
     */
    @PutMapping("/{id}")
    public Result<Address> update(@PathVariable Long id, @RequestBody Address address, @RequestHeader("Authorization") String token) {
        // 从token中解析用户ID，确保用户只能更新自己的地址
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        Address existingAddress = addressService.findById(id);

        if (existingAddress == null || !existingAddress.getUserId().equals(userId)) {
            return Result.error("无权修改该地址");
        }
        
        address.setId(id);
        address.setUserId(userId); // 确保userId被设置
        Address updatedAddress = addressService.update(address);
        if (updatedAddress != null) {
            return Result.success("地址更新成功", updatedAddress);
        } else {
            return Result.error("地址更新失败");
        }
    }
    
    /**
     * 设置默认地址
     */
    @PutMapping("/{id}/default")
    public Result<String> setDefault(@PathVariable Long id) {
        boolean success = addressService.setDefault(id);
        if (success) {
            return Result.success("默认地址设置成功");
        } else {
            return Result.error("设置默认地址失败");
        }
    }
    
    /**
     * 删除地址
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = addressService.delete(id);
        if (success) {
            return Result.success("地址删除成功");
        } else {
            return Result.error("地址删除失败");
        }
    }
} 