package com.logistics.service;

import com.logistics.entity.Expense;
import com.logistics.mapper.ExpenseMapper;
import com.logistics.common.PageRequest;
import com.logistics.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.math.BigDecimal;
import lombok.Data;

@Service
public class ExpenseService {
    
    @Autowired
    private ExpenseMapper expenseMapper;
    
    /**
     * 费用统计内部类
     */
    @Data
    public static class ExpenseStatistics {
        private long totalCount;
        private long pendingCount;
        private long approvedCount;
        private long rejectedCount;
        private BigDecimal totalAmount;
        private BigDecimal approvedAmount;
        private BigDecimal pendingAmount;
        
        public ExpenseStatistics() {}
        
        public ExpenseStatistics(long totalCount, long pendingCount, long approvedCount, 
                               long rejectedCount, BigDecimal totalAmount, BigDecimal approvedAmount, 
                               BigDecimal pendingAmount) {
            this.totalCount = totalCount;
            this.pendingCount = pendingCount;
            this.approvedCount = approvedCount;
            this.rejectedCount = rejectedCount;
            this.totalAmount = totalAmount;
            this.approvedAmount = approvedAmount;
            this.pendingAmount = pendingAmount;
        }
    }
    
    public List<Expense> findAll() {
        return expenseMapper.findAll();
    }
    
    /**
     * 分页查询费用
     */
    public PageResult<Expense> findByPage(PageRequest pageRequest) {
        // 检查是否有搜索条件
        boolean hasConditions = (pageRequest.getKeyword() != null && !pageRequest.getKeyword().trim().isEmpty()) ||
                              (pageRequest.getExpenseType() != null && !pageRequest.getExpenseType().trim().isEmpty()) ||
                              (pageRequest.getApprovalStatus() != null && !pageRequest.getApprovalStatus().trim().isEmpty());
        
        if (hasConditions) {
            // 有搜索条件，使用多条件查询
            List<Expense> expenses = expenseMapper.findByPageWithConditions(
                pageRequest.getKeyword(),
                pageRequest.getExpenseType(),
                pageRequest.getApprovalStatus(),
                pageRequest.getOffset(),
                pageRequest.getPageSize()
            );
            
            int total = expenseMapper.countWithConditions(
                pageRequest.getKeyword(),
                pageRequest.getExpenseType(),
                pageRequest.getApprovalStatus()
            );
            
            int totalPages = (int) Math.ceil((double) total / pageRequest.getPageSize());
            return new PageResult<>(expenses, total, pageRequest.getPageNum(), totalPages);
        } else {
            // 无搜索条件，查询所有数据
            List<Expense> allExpenses = expenseMapper.findAll();
            int total = allExpenses.size();
            int totalPages = (int) Math.ceil((double) total / pageRequest.getPageSize());
            
            // 手动分页
            int start = pageRequest.getOffset();
            int end = Math.min(start + pageRequest.getPageSize(), total);
            List<Expense> expenses = allExpenses.subList(start, end);
            
            return new PageResult<>(expenses, total, pageRequest.getPageNum(), totalPages);
        }
    }
    
    /**
     * 根据类型查询费用
     */
    public List<Expense> findByType(String type) {
        return expenseMapper.findByExpenseType(type);
    }
    
    /**
     * 根据状态查询费用
     */
    public List<Expense> findByStatus(String status) {
        return expenseMapper.findByApprovalStatus(status);
    }
    
    public Expense findById(Long id) {
        return expenseMapper.findById(id.intValue());
    }
    
    public List<Expense> findByTransportId(Long transportId) {
        return expenseMapper.findByTransportId(transportId.intValue());
    }
    
    public List<Expense> findByExpenseType(String expenseType) {
        return expenseMapper.findByExpenseType(expenseType);
    }
    
    public List<Expense> findByApprovalStatus(String status) {
        return expenseMapper.findByApprovalStatus(status);
    }
    
    public List<Expense> findByApproverId(Integer approverId) {
        return expenseMapper.findByApproverId(approverId);
    }
    
    public List<Expense> searchByKeyword(String keyword) {
        return expenseMapper.searchByKeyword(keyword);
    }
    
    public boolean save(Expense expense) {
        try {
            if (expense.getId() == null) {
                // 新增
                if (expense.getApprovalStatus() == null) {
                    expense.setApprovalStatus("pending");
                }
                expenseMapper.insert(expense);
            } else {
                // 更新
                expenseMapper.update(expense);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 更新费用状态
     */
    public boolean updateStatus(Expense expense) {
        try {
            return expenseMapper.updateApprovalStatus(expense.getId(), expense.getApprovalStatus(), expense.getApprovedBy()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 删除费用
     */
    public boolean delete(Long id) {
        try {
            return expenseMapper.deleteById(id.intValue()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean updateApprovalStatus(Integer id, String status, Integer approverId) {
        return expenseMapper.updateApprovalStatus(id, status, approverId) > 0;
    }
    
    public boolean approve(Integer id, Integer approverId) {
        return updateApprovalStatus(id, "approved", approverId);
    }
    
    public boolean reject(Integer id, Integer approverId) {
        return updateApprovalStatus(id, "rejected", approverId);
    }
    
    public boolean deleteById(Integer id) {
        return expenseMapper.deleteById(id) > 0;
    }
    
    public int count() {
        return expenseMapper.count();
    }
    
    public int countByStatus(String status) {
        return expenseMapper.countByStatus(status);
    }
    
    public BigDecimal getTotalApprovedAmount() {
        BigDecimal total = expenseMapper.getTotalApprovedAmount();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalExpenseByTransport(Integer transportId) {
        BigDecimal total = expenseMapper.getTotalExpenseByTransport(transportId);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public List<Expense> findPendingExpenses() {
        return expenseMapper.findByApprovalStatus("pending");
    }
    
    public List<Expense> findApprovedExpenses() {
        return expenseMapper.findByApprovalStatus("approved");
    }
    
    public List<Expense> findRejectedExpenses() {
        return expenseMapper.findByApprovalStatus("rejected");
    }
    
    /**
     * 获取费用统计信息
     */
    public ExpenseStatistics getExpenseStatistics() {
        long totalCount = count();
        long pendingCount = countByStatus("pending");
        long approvedCount = countByStatus("approved");
        long rejectedCount = countByStatus("rejected");
        BigDecimal totalAmount = getTotalApprovedAmount();
        BigDecimal approvedAmount = getTotalApprovedAmount();
        
        // 这里可以添加更多统计逻辑
        BigDecimal pendingAmount = BigDecimal.ZERO; // 需要根据实际需求计算待审核金额
        
        return new ExpenseStatistics(totalCount, pendingCount, approvedCount, rejectedCount, 
                                   totalAmount, approvedAmount, pendingAmount);
    }
} 