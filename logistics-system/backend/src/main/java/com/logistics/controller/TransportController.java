package com.logistics.controller;

import com.logistics.entity.Transport;
import com.logistics.service.TransportService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运输控制器
 */
@RestController
@RequestMapping("/transports")
@CrossOrigin
public class TransportController {
    
    @Autowired
    private TransportService transportService;
    
    @PostMapping("/page")
    public Result<PageResult<Transport>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Transport> result = transportService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    @GetMapping("/status/{status}")
    public Result<List<Transport>> getByStatus(@PathVariable String status) {
        List<Transport> transports = transportService.findByStatus(status);
        return Result.success(transports);
    }
    
    @GetMapping("/number/{transportNumber}")
    public Result<Transport> getByTransportNumber(@PathVariable String transportNumber) {
        Transport transport = transportService.findByTransportNumber(transportNumber);
        if (transport != null) {
            return Result.success(transport);
        } else {
            return Result.error("运输任务不存在");
        }
    }
    
    @GetMapping("/{id}")
    public Result<Transport> getById(@PathVariable Long id) {
        Transport transport = transportService.findById(id);
        if (transport != null) {
            return Result.success(transport);
        } else {
            return Result.error("运输任务不存在");
        }
    }
    
    @PostMapping
    public Result<String> save(@RequestBody Transport transport) {
        boolean success = transportService.save(transport);
        if (success) {
            return Result.success(transport.getId() == null ? "运输任务创建成功" : "运输任务更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody String status) {
        boolean success = transportService.updateStatus(id, status);
        if (success) {
            return Result.success("运输状态更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = transportService.delete(id);
        if (success) {
            return Result.success("运输任务删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 