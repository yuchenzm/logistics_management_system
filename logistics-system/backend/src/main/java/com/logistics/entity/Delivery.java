package com.logistics.entity;

import java.time.LocalDateTime;

public class Delivery {
    private Integer id;
    private Integer transportId;
    private String deliveryAddress;
    private String deliveryContact;
    private String deliveryPhone;
    private LocalDateTime scheduledTime;
    private LocalDateTime actualDeliveryTime;
    private String deliveryStatus; // pending, out_for_delivery, delivered, failed, returned
    private Boolean signatureRequired;
    private String recipientName;
    private String deliveryNotes;
    private String proofOfDelivery;
    private Integer attempts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 构造函数
    public Delivery() {}

    public Delivery(Integer transportId, String deliveryAddress, String deliveryContact, String deliveryPhone) {
        this.transportId = transportId;
        this.deliveryAddress = deliveryAddress;
        this.deliveryContact = deliveryContact;
        this.deliveryPhone = deliveryPhone;
        this.signatureRequired = true;
        this.attempts = 0;
        this.deliveryStatus = "pending";
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTransportId() { return transportId; }
    public void setTransportId(Integer transportId) { this.transportId = transportId; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getDeliveryContact() { return deliveryContact; }
    public void setDeliveryContact(String deliveryContact) { this.deliveryContact = deliveryContact; }

    public String getDeliveryPhone() { return deliveryPhone; }
    public void setDeliveryPhone(String deliveryPhone) { this.deliveryPhone = deliveryPhone; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public LocalDateTime getActualDeliveryTime() { return actualDeliveryTime; }
    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) { this.actualDeliveryTime = actualDeliveryTime; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    // 为Service层兼容性添加的别名方法
    public String getStatus() { return deliveryStatus; }
    public void setStatus(String status) { this.deliveryStatus = status; }

    public String getRecipient() { return recipientName; }

    public Boolean getSignatureRequired() { return signatureRequired; }
    public void setSignatureRequired(Boolean signatureRequired) { this.signatureRequired = signatureRequired; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getDeliveryNotes() { return deliveryNotes; }
    public void setDeliveryNotes(String deliveryNotes) { this.deliveryNotes = deliveryNotes; }

    public String getProofOfDelivery() { return proofOfDelivery; }
    public void setProofOfDelivery(String proofOfDelivery) { this.proofOfDelivery = proofOfDelivery; }

    public Integer getAttempts() { return attempts; }
    public void setAttempts(Integer attempts) { this.attempts = attempts; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 