package com.logistics.controller;

import com.logistics.entity.Feedback;
import com.logistics.service.FeedbackService;
import com.logistics.common.Result;
import com.logistics.common.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 反馈控制器
 */
@RestController
@RequestMapping("/feedback")
@CrossOrigin
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 根据用户ID获取反馈列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<Feedback>> getByUserId(@PathVariable Long userId) {
        List<Feedback> feedbacks = feedbackService.findByUserId(userId);
        return Result.success(feedbacks);
    }
    
    /**
     * 根据ID获取反馈
     */
    @GetMapping("/{id}")
    public Result<Feedback> getById(@PathVariable Long id) {
        Feedback feedback = feedbackService.findById(id);
        if (feedback != null) {
            return Result.success(feedback);
        } else {
            return Result.error("反馈不存在");
        }
    }
    
    /**
     * 创建反馈
     */
    @PostMapping
    public Result<Feedback> create(@RequestBody Feedback feedback, @RequestHeader("Authorization") String token) {
        Long userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        feedback.setUserId(userId);

        Feedback createdFeedback = feedbackService.create(feedback);
        if (createdFeedback != null) {
            return Result.success("反馈提交成功", createdFeedback);
        } else {
            return Result.error("反馈提交失败");
        }
    }
    
    /**
     * 更新反馈状态
     */
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");
        boolean success = feedbackService.updateStatus(id, status);
        if (success) {
            return Result.success("反馈状态更新成功");
        } else {
            return Result.error("反馈状态更新失败");
        }
    }
    
    /**
     * 删除反馈
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = feedbackService.delete(id);
        if (success) {
            return Result.success("反馈删除成功");
        } else {
            return Result.error("反馈删除失败");
        }
    }
    
    /**
     * 获取所有反馈（管理员用）
     */
    @GetMapping("/all")
    public Result<List<Feedback>> getAll() {
        List<Feedback> feedbacks = feedbackService.findAll();
        return Result.success(feedbacks);
    }
    
    /**
     * 根据状态查找反馈
     */
    @GetMapping("/status/{status}")
    public Result<List<Feedback>> getByStatus(@PathVariable String status) {
        List<Feedback> feedbacks = feedbackService.findByStatus(status);
        return Result.success(feedbacks);
    }
} 