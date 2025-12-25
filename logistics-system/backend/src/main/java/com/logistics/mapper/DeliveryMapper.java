package com.logistics.mapper;

import com.logistics.entity.Delivery;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DeliveryMapper {
    
    @Select("SELECT * FROM deliveries")
    List<Delivery> findAll();
    
    @Select("SELECT * FROM deliveries WHERE id = #{id}")
    Delivery findById(Integer id);
    
    @Select("SELECT * FROM deliveries WHERE transport_id = #{transportId}")
    List<Delivery> findByTransportId(Integer transportId);
    
    @Select("SELECT * FROM deliveries WHERE delivery_status = #{status}")
    List<Delivery> findByStatus(String status);
    
    @Select("SELECT * FROM deliveries WHERE delivery_contact = #{deliverer}")
    List<Delivery> findByDeliverer(String deliverer);
    
    @Select("SELECT * FROM deliveries WHERE recipient_name = #{recipient}")
    List<Delivery> findByRecipient(String recipient);
    
    @Select("SELECT d.* FROM deliveries d " +
            "JOIN transports t ON d.transport_id = t.id " +
            "WHERE t.transport_number = #{transportNumber}")
    List<Delivery> findByTransportNumber(String transportNumber);
    
    @Select("SELECT * FROM deliveries WHERE delivery_contact LIKE CONCAT('%', #{keyword}, '%') " +
            "OR delivery_phone LIKE CONCAT('%', #{keyword}, '%') " +
            "OR recipient_name LIKE CONCAT('%', #{keyword}, '%')")
    List<Delivery> searchByKeyword(String keyword);
    
    @Insert("INSERT INTO deliveries (transport_id, delivery_address, delivery_contact, delivery_phone, " +
            "scheduled_time, delivery_status, signature_required, recipient_name, delivery_notes, attempts) " +
            "VALUES (#{transportId}, #{deliveryAddress}, #{deliveryContact}, #{deliveryPhone}, " +
            "#{scheduledTime}, #{deliveryStatus}, #{signatureRequired}, #{recipientName}, #{deliveryNotes}, #{attempts})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Delivery delivery);
    
    @Update("UPDATE deliveries SET transport_id = #{transportId}, delivery_address = #{deliveryAddress}, " +
            "delivery_contact = #{deliveryContact}, delivery_phone = #{deliveryPhone}, " +
            "scheduled_time = #{scheduledTime}, actual_delivery_time = #{actualDeliveryTime}, " +
            "delivery_status = #{deliveryStatus}, signature_required = #{signatureRequired}, " +
            "recipient_name = #{recipientName}, delivery_notes = #{deliveryNotes}, " +
            "proof_of_delivery = #{proofOfDelivery}, attempts = #{attempts}, updated_at = NOW() " +
            "WHERE id = #{id}")
    int update(Delivery delivery);
    
    @Update("UPDATE deliveries SET delivery_status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
    
    @Update("UPDATE deliveries SET actual_delivery_time = NOW(), delivery_status = 'delivered', " +
            "proof_of_delivery = #{proofOfDelivery}, updated_at = NOW() WHERE id = #{id}")
    int markAsDelivered(@Param("id") Integer id, @Param("proofOfDelivery") String proofOfDelivery);
    
    @Update("UPDATE deliveries SET actual_delivery_time = NOW(), delivery_status = 'delivered', " +
            "recipient_name = #{recipient}, signature_required = #{signatureRequired}, updated_at = NOW() WHERE id = #{id}")
    int confirmDelivery(@Param("id") Integer id, @Param("recipient") String recipient, @Param("signatureRequired") Boolean signatureRequired);
    
    @Update("UPDATE deliveries SET attempts = attempts + 1, delivery_status = 'failed', " +
            "delivery_notes = #{notes}, updated_at = NOW() WHERE id = #{id}")
    int markAsFailed(@Param("id") Integer id, @Param("notes") String notes);
    
    @Delete("DELETE FROM deliveries WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Select("SELECT COUNT(*) FROM deliveries")
    int count();
    
    @Select("SELECT COUNT(*) FROM deliveries WHERE delivery_status = #{status}")
    int countByStatus(String status);
} 