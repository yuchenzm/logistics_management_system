package com.logistics.service;

import com.logistics.entity.Feedback;
import com.logistics.mapper.FeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 反馈服务类
 */
@Service
public class FeedbackService {
    
    @Autowired
    private FeedbackMapper feedbackMapper;
    
    public List<Feedback> findByUserId(Long userId) {
            return feedbackMapper.findByUserId(userId);
    }
    
    public Feedback findById(Long id) {
            return feedbackMapper.findById(id);
    }
    
    public Feedback create(Feedback feedback) {
            feedback.setCreateTime(LocalDateTime.now());
            feedback.setUpdateTime(LocalDateTime.now());
        feedback.setStatus("pending"); // 默认状态
            feedbackMapper.save(feedback);
            return feedback;
    }
    
    public boolean updateStatus(Long id, String status) {
            return feedbackMapper.updateStatus(id, status) > 0;
    }
    
    public boolean delete(Long id) {
            return feedbackMapper.delete(id) > 0;
    }
    
    public List<Feedback> findAll() {
            return feedbackMapper.findAll();
    }
    
    public List<Feedback> findByStatus(String status) {
            return feedbackMapper.findByStatus(status);
    }
} 