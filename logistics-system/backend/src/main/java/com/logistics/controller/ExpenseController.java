package com.logistics.controller;

import com.logistics.entity.Expense;
import com.logistics.service.ExpenseService;
import com.logistics.common.Result;
import com.logistics.common.PageResult;
import com.logistics.common.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 费用控制器
 */
@RestController
@RequestMapping("/expenses")
@CrossOrigin
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;
    
    /**
     * 获取所有费用
     */
    @GetMapping
    public Result<List<Expense>> getAll() {
        List<Expense> expenses = expenseService.findAll();
        return Result.success(expenses);
    }
    
    /**
     * 分页查询费用
     */
    @PostMapping("/page")
    public Result<PageResult<Expense>> getByPage(@RequestBody PageRequest pageRequest) {
        PageResult<Expense> result = expenseService.findByPage(pageRequest);
        return Result.success(result);
    }
    
    /**
     * 根据类型查询费用
     */
    @GetMapping("/type/{type}")
    public Result<List<Expense>> getByType(@PathVariable String type) {
        List<Expense> expenses = expenseService.findByType(type);
        return Result.success(expenses);
    }
    
    /**
     * 根据运输ID查询费用
     */
    @GetMapping("/transport/{transportId}")
    public Result<List<Expense>> getByTransportId(@PathVariable Long transportId) {
        List<Expense> expenses = expenseService.findByTransportId(transportId);
        return Result.success(expenses);
    }
    
    /**
     * 根据状态查询费用
     */
    @GetMapping("/status/{status}")
    public Result<List<Expense>> getByStatus(@PathVariable String status) {
        List<Expense> expenses = expenseService.findByStatus(status);
        return Result.success(expenses);
    }
    
    /**
     * 根据关键词搜索费用
     */
    @GetMapping("/search")
    public Result<List<Expense>> search(@RequestParam String keyword) {
        List<Expense> expenses = expenseService.searchByKeyword(keyword);
        return Result.success(expenses);
    }
    
    /**
     * 获取费用统计
     */
    @GetMapping("/statistics")
    public Result<ExpenseService.ExpenseStatistics> getStatistics() {
        ExpenseService.ExpenseStatistics statistics = expenseService.getExpenseStatistics();
        return Result.success(statistics);
    }
    
    /**
     * 根据ID查询费用
     */
    @GetMapping("/{id}")
    public Result<Expense> getById(@PathVariable Long id) {
        Expense expense = expenseService.findById(id);
        if (expense != null) {
            return Result.success(expense);
        } else {
            return Result.error("费用记录不存在");
        }
    }
    
    /**
     * 保存费用（新增或更新）
     */
    @PostMapping
    public Result<String> save(@RequestBody Expense expense) {
        boolean success = expenseService.save(expense);
        if (success) {
            return Result.success(expense.getId() == null ? "费用记录创建成功" : "费用记录更新成功");
        } else {
            return Result.error("操作失败");
        }
    }
    
    /**
     * 更新费用状态
     */
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Expense expense) {
        expense.setId(id.intValue());
        boolean success = expenseService.updateStatus(expense);
        if (success) {
            return Result.success("费用状态更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
    
    /**
     * 删除费用
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = expenseService.delete(id);
        if (success) {
            return Result.success("费用记录删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
} 