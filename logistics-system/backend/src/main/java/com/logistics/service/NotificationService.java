package com.logistics.service;

import com.logistics.entity.Notification;
import com.logistics.mapper.NotificationMapper;
import com.logistics.common.PageRequest;
import com.logistics.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知服务类
 */
@Service
public class NotificationService {
    
    @Autowired
    private NotificationMapper notificationMapper;
    
    /**
     * 根据用户ID获取通知列表
     */
    public List<Notification> findByUserId(Long userId) {
            return notificationMapper.findByUserId(userId);
    }

    /**
     * 分页查询用户通知
     */
    public PageResult<Notification> findByUserIdAndPage(Long userId, PageRequest pageRequest) {
            List<Notification> notifications = notificationMapper.findByUserIdAndPage(userId, pageRequest.getOffset(), pageRequest.getPageSize());
            long total = notificationMapper.countByUserId(userId);
            return new PageResult<>(notifications, total, pageRequest.getPageNum(), pageRequest.getPageSize());
    }
    
    /**
     * 根据ID获取通知
     */
    public Notification findById(Long id) {
            return notificationMapper.findById(id);
    }
    
    /**
     * 创建通知
     */
    public Notification create(Notification notification) {
            notification.setCreateTime(LocalDateTime.now());
            notificationMapper.save(notification);
            return notification;
    }
    
    /**
     * 标记为已读
     */
    public boolean markAsRead(Long id) {
            return notificationMapper.markAsRead(id) > 0;
    }
    
    /**
     * 批量标记为已读
     */
    public boolean batchMarkAsRead(List<Long> ids) {
            return notificationMapper.batchMarkAsRead(ids) > 0;
    }

    /**
     * 根据用户ID将所有通知标记为已读
     */
    public boolean markAllAsReadByUserId(Long userId) {
        return notificationMapper.markAllAsReadByUserId(userId) > 0;
    }
    
    /**
     * 删除通知
     */
    public boolean delete(Long id) {
            return notificationMapper.delete(id) > 0;
    }

    /**
     * 根据用户ID删除所有通知
     */
    public boolean deleteAllByUserId(Long userId) {
        return notificationMapper.deleteAllByUserId(userId) > 0;
    }
    
    /**
     * 获取未读通知数量
     */
    public int getUnreadCount(Long userId) {
            return notificationMapper.getUnreadCount(userId);
    }
} 