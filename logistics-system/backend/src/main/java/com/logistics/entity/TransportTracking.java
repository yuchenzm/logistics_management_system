package com.logistics.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransportTracking {
    private Integer id;
    private Integer transportId;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String statusUpdate;
    private LocalDateTime trackingTime;
    private String notes;

    // 构造函数
    public TransportTracking() {}

    public TransportTracking(Integer transportId, String location, String statusUpdate) {
        this.transportId = transportId;
        this.location = location;
        this.statusUpdate = statusUpdate;
        this.trackingTime = LocalDateTime.now();
    }

    // 业务方法
    public boolean hasCoordinates() {
        return latitude != null && longitude != null;
    }

    public String getCoordinatesString() {
        if (hasCoordinates()) {
            return latitude + "," + longitude;
        }
        return null;
    }

    public void setCoordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTransportId() { return transportId; }
    public void setTransportId(Integer transportId) { this.transportId = transportId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public String getStatusUpdate() { return statusUpdate; }
    public void setStatusUpdate(String statusUpdate) { this.statusUpdate = statusUpdate; }

    public LocalDateTime getTrackingTime() { return trackingTime; }
    public void setTrackingTime(LocalDateTime trackingTime) { this.trackingTime = trackingTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 