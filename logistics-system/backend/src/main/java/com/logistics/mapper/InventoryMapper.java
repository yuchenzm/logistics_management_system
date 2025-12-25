package com.logistics.mapper;

import com.logistics.entity.Inventory;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface InventoryMapper {
    
    @Select("SELECT * FROM inventory")
    List<Inventory> findAll();
    
    @Select("SELECT * FROM inventory WHERE id = #{id}")
    Inventory findById(Integer id);
    
    @Select("SELECT * FROM inventory WHERE warehouse_id = #{warehouseId}")
    List<Inventory> findByWarehouseId(Integer warehouseId);
    
    @Select("SELECT * FROM inventory WHERE goods_id = #{goodsId}")
    List<Inventory> findByGoodsId(Integer goodsId);
    
    @Select("SELECT * FROM inventory WHERE warehouse_id = #{warehouseId} AND goods_id = #{goodsId}")
    Inventory findByWarehouseAndGoods(@Param("warehouseId") Integer warehouseId, @Param("goodsId") Integer goodsId);
    
    @Select("SELECT * FROM inventory WHERE batch_number = #{batchNumber}")
    List<Inventory> findByBatchNumber(String batchNumber);
    
    @Select("SELECT * FROM inventory WHERE location LIKE CONCAT('%', #{location}, '%')")
    List<Inventory> findByLocation(String location);
    
    @Select("SELECT * FROM inventory WHERE quantity > 0")
    List<Inventory> findAvailableStock();
    
    @Select("SELECT * FROM inventory WHERE (quantity - reserved_quantity) > 0")
    List<Inventory> findAvailableForReservation();
    
    @Select("SELECT * FROM inventory WHERE expiry_date < CURDATE() + INTERVAL #{days} DAY")
    List<Inventory> findExpiringItems(Integer days);
    
    @Insert("INSERT INTO inventory (warehouse_id, goods_id, quantity, reserved_quantity, location, " +
            "batch_number, expiry_date) " +
            "VALUES (#{warehouseId}, #{goodsId}, #{quantity}, #{reservedQuantity}, #{location}, " +
            "#{batchNumber}, #{expiryDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Inventory inventory);
    
    @Update("UPDATE inventory SET warehouse_id = #{warehouseId}, goods_id = #{goodsId}, " +
            "quantity = #{quantity}, reserved_quantity = #{reservedQuantity}, location = #{location}, " +
            "batch_number = #{batchNumber}, expiry_date = #{expiryDate}, last_updated = NOW() " +
            "WHERE id = #{id}")
    int update(Inventory inventory);
    
    @Update("UPDATE inventory SET quantity = #{quantity}, last_updated = NOW() WHERE id = #{id}")
    int updateQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);
    
    @Update("UPDATE inventory SET reserved_quantity = #{reservedQuantity}, last_updated = NOW() WHERE id = #{id}")
    int updateReservedQuantity(@Param("id") Integer id, @Param("reservedQuantity") Integer reservedQuantity);
    
    @Update("UPDATE inventory SET quantity = quantity + #{amount}, last_updated = NOW() WHERE id = #{id}")
    int addStock(@Param("id") Integer id, @Param("amount") Integer amount);
    
    @Update("UPDATE inventory SET quantity = GREATEST(0, quantity - #{amount}), last_updated = NOW() WHERE id = #{id}")
    int removeStock(@Param("id") Integer id, @Param("amount") Integer amount);
    
    @Update("UPDATE inventory SET reserved_quantity = reserved_quantity + #{amount}, last_updated = NOW() " +
            "WHERE id = #{id} AND (quantity - reserved_quantity) >= #{amount}")
    int reserveStock(@Param("id") Integer id, @Param("amount") Integer amount);
    
    @Update("UPDATE inventory SET reserved_quantity = GREATEST(0, reserved_quantity - #{amount}), " +
            "last_updated = NOW() WHERE id = #{id}")
    int releaseReservedStock(@Param("id") Integer id, @Param("amount") Integer amount);
    
    @Update("UPDATE inventory SET actual_quantity = actual_quantity + #{adjustment}, last_updated = NOW() WHERE id = #{id}")
    int adjustQuantity(@Param("id") Integer id, @Param("adjustment") Integer adjustment);
    
    @Delete("DELETE FROM inventory WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Select("SELECT COUNT(*) FROM inventory")
    int count();
    
    @Select("SELECT COUNT(*) FROM inventory WHERE warehouse_id = #{warehouseId}")
    int countByWarehouse(Integer warehouseId);
    
    @Select("SELECT COALESCE(SUM(actual_quantity), 0) FROM inventory")
    int getTotalStock();
    
    @Select("SELECT COALESCE(SUM(actual_quantity - reserved_quantity), 0) FROM inventory")
    int getTotalAvailableStock();
    
    @Select("SELECT COALESCE(SUM(reserved_quantity), 0) FROM inventory")
    int getTotalReservedStock();
    
    @Select("SELECT SUM(quantity) FROM inventory WHERE goods_id = #{goodsId}")
    Integer getTotalQuantityByGoods(Integer goodsId);
    
    @Select("SELECT SUM(quantity - reserved_quantity) FROM inventory WHERE goods_id = #{goodsId}")
    Integer getAvailableQuantityByGoods(Integer goodsId);
    
    @Select("SELECT i.*, w.name as warehouse_name, g.name as goods_name " +
            "FROM inventory i " +
            "LEFT JOIN warehouses w ON i.warehouse_id = w.id " +
            "LEFT JOIN goods g ON i.goods_id = g.id " +
            "WHERE w.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR g.name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR i.batch_number LIKE CONCAT('%', #{keyword}, '%') " +
            "OR i.location LIKE CONCAT('%', #{keyword}, '%')")
    List<Inventory> searchByKeyword(String keyword);
    
    @Select("SELECT * FROM inventory WHERE actual_quantity <= #{threshold}")
    List<Inventory> findLowStockItems(Integer threshold);
    
    @Select("SELECT * FROM inventory WHERE expiry_date < CURDATE()")
    List<Inventory> findExpiredItems();
} 