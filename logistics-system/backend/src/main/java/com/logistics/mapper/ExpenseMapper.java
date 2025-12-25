package com.logistics.mapper;

import com.logistics.entity.Expense;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.math.BigDecimal;

@Mapper
public interface ExpenseMapper {
    
    @Select("SELECT * FROM expenses")
    List<Expense> findAll();
    
    @Select("SELECT * FROM expenses WHERE id = #{id}")
    Expense findById(Integer id);
    
    @Select("SELECT * FROM expenses WHERE transport_id = #{transportId}")
    List<Expense> findByTransportId(Integer transportId);
    
    @Select("SELECT * FROM expenses WHERE expense_type = #{expenseType}")
    List<Expense> findByExpenseType(String expenseType);
    
    @Select("SELECT * FROM expenses WHERE approval_status = #{status}")
    List<Expense> findByApprovalStatus(String status);
    
    @Select("SELECT * FROM expenses WHERE approved_by = #{approverId}")
    List<Expense> findByApproverId(Integer approverId);
    
    @Select("SELECT * FROM expenses WHERE description LIKE CONCAT('%', #{keyword}, '%') " +
            "OR receipt_number LIKE CONCAT('%', #{keyword}, '%')")
    List<Expense> searchByKeyword(String keyword);
    
    @Insert("INSERT INTO expenses (transport_id, expense_type, amount, description, receipt_number, " +
            "expense_date, approved_by, approval_status) " +
            "VALUES (#{transportId}, #{expenseType}, #{amount}, #{description}, #{receiptNumber}, " +
            "#{expenseDate}, #{approvedBy}, #{approvalStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Expense expense);
    
    @Update("UPDATE expenses SET transport_id = #{transportId}, expense_type = #{expenseType}, " +
            "amount = #{amount}, description = #{description}, receipt_number = #{receiptNumber}, " +
            "expense_date = #{expenseDate}, approved_by = #{approvedBy}, " +
            "approval_status = #{approvalStatus}, updated_at = NOW() WHERE id = #{id}")
    int update(Expense expense);
    
    @Update("UPDATE expenses SET approval_status = #{status}, approved_by = #{approverId}, " +
            "updated_at = NOW() WHERE id = #{id}")
    int updateApprovalStatus(@Param("id") Integer id, @Param("status") String status, @Param("approverId") Integer approverId);
    
    @Delete("DELETE FROM expenses WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Select("SELECT COUNT(*) FROM expenses")
    int count();
    
    @Select("SELECT COUNT(*) FROM expenses WHERE approval_status = #{status}")
    int countByStatus(String status);
    
    @Select("SELECT SUM(amount) FROM expenses WHERE approval_status = 'approved'")
    BigDecimal getTotalApprovedAmount();
    
    @Select("SELECT SUM(amount) FROM expenses WHERE transport_id = #{transportId} AND approval_status = 'approved'")
    BigDecimal getTotalExpenseByTransport(Integer transportId);
    
    // 多条件分页查询
    @Select("<script>" +
            "SELECT * FROM expenses WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (description LIKE CONCAT('%', #{keyword}, '%') " +
            "OR receipt_number LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='expenseType != null and expenseType != \"\"'>" +
            "AND expense_type = #{expenseType} " +
            "</if>" +
            "<if test='approvalStatus != null and approvalStatus != \"\"'>" +
            "AND approval_status = #{approvalStatus} " +
            "</if>" +
            "ORDER BY created_at DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Expense> findByPageWithConditions(@Param("keyword") String keyword,
                                         @Param("expenseType") String expenseType,
                                         @Param("approvalStatus") String approvalStatus,
                                         @Param("offset") int offset,
                                         @Param("pageSize") int pageSize);
    
    // 多条件计数查询
    @Select("<script>" +
            "SELECT COUNT(*) FROM expenses WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (description LIKE CONCAT('%', #{keyword}, '%') " +
            "OR receipt_number LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='expenseType != null and expenseType != \"\"'>" +
            "AND expense_type = #{expenseType} " +
            "</if>" +
            "<if test='approvalStatus != null and approvalStatus != \"\"'>" +
            "AND approval_status = #{approvalStatus} " +
            "</if>" +
            "</script>")
    int countWithConditions(@Param("keyword") String keyword,
                          @Param("expenseType") String expenseType,
                          @Param("approvalStatus") String approvalStatus);
} 