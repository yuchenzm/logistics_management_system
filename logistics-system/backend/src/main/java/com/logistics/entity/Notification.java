package com.logistics.entity;

import java.time.LocalDateTime;

/**
 * 通知实体类
 */
public class Notification {
    private Long id;
    private Long userId;           // 接收用户ID
    private String type;           // 通知类型：system-系统通知，order-订单通知，transport-运输通知
    private String title;          // 通知标题
    private String content;        // 通知内容
    private String status;         // 状态：unread-未读，read-已读
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime readTime;   // 阅读时间

    // 构造函数
    public Notification() {}

    public Notification(Long userId, String type, String title, String content) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.status = "unread";
        this.createTime = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(LocalDateTime readTime) {
        this.readTime = readTime;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", readTime=" + readTime +
                '}';
    }
} 