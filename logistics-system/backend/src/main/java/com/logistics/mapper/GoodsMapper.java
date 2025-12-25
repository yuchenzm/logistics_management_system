package com.logistics.mapper;

import com.logistics.entity.Goods;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface GoodsMapper {
    
    @Select("SELECT * FROM goods")
    List<Goods> findAll();
    
    @Select("SELECT * FROM goods WHERE id = #{id}")
    Goods findById(Integer id);
    
    @Select("SELECT * FROM goods WHERE category = #{category}")
    List<Goods> findByCategory(String category);
    
    @Select("SELECT * FROM goods WHERE status = #{status}")
    List<Goods> findByStatus(String status);
    
    @Select("SELECT * FROM goods WHERE sku = #{sku}")
    Goods findBySku(String sku);
    
    @Select("SELECT * FROM goods WHERE warehouse_id = #{warehouseId}")
    List<Goods> findByWarehouseId(Integer warehouseId);
    
    @Select("SELECT * FROM goods WHERE fragile = 1")
    List<Goods> findFragileGoods();
    
    @Select("SELECT * FROM goods WHERE hazardous = 1")
    List<Goods> findDangerousGoods();
    
    @Select("SELECT * FROM goods WHERE temperature_requirements = #{temperatureRequirements}")
    List<Goods> findByTemperatureRequirement(String temperatureRequirements);
    
    @Select("SELECT * FROM goods WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    List<Goods> searchByKeyword(String keyword);
    
    @Insert("INSERT INTO goods (name, sku, category, description, weight, volume, unit_price, fragile, hazardous, temperature_requirements, supplier_id, status) VALUES (#{name}, #{sku}, #{category}, #{description}, #{weight}, #{volume}, #{unitPrice}, #{fragile}, #{hazardous}, #{temperatureRequirements}, #{supplierId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Goods goods);
    
    @Update("UPDATE goods SET name = #{name}, sku = #{sku}, category = #{category}, description = #{description}, weight = #{weight}, volume = #{volume}, unit_price = #{unitPrice}, fragile = #{fragile}, hazardous = #{hazardous}, temperature_requirements = #{temperatureRequirements}, supplier_id = #{supplierId}, status = #{status} WHERE id = #{id}")
    int update(Goods goods);
    
    @Update("UPDATE goods SET status = #{status} WHERE id = #{id}")
    int updateStatus(Integer id, String status);
    
    @Delete("DELETE FROM goods WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Select("SELECT COUNT(*) FROM goods")
    int count();
    
    @Select("SELECT COUNT(*) FROM goods WHERE category = #{category}")
    int countByCategory(String category);
    
    @Select("SELECT COUNT(*) FROM goods WHERE status = #{status}")
    int countByStatus(String status);
    
    // 多条件分页查询
    @Select("<script>" +
            "SELECT * FROM goods WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR sku LIKE CONCAT('%', #{keyword}, '%') " +
            "OR description LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='goodsCategory != null and goodsCategory != \"\"'>" +
            "AND category = #{goodsCategory} " +
            "</if>" +
            "<if test='goodsStatus != null and goodsStatus != \"\"'>" +
            "AND status = #{goodsStatus} " +
            "</if>" +
            "ORDER BY created_at DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Goods> findByPageWithConditions(@Param("keyword") String keyword,
                                        @Param("goodsCategory") String goodsCategory,
                                        @Param("goodsStatus") String goodsStatus,
                                        @Param("offset") int offset,
                                        @Param("pageSize") int pageSize);
    
    // 多条件计数查询
    @Select("<script>" +
            "SELECT COUNT(*) FROM goods WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR sku LIKE CONCAT('%', #{keyword}, '%') " +
            "OR description LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "<if test='goodsCategory != null and goodsCategory != \"\"'>" +
            "AND category = #{goodsCategory} " +
            "</if>" +
            "<if test='goodsStatus != null and goodsStatus != \"\"'>" +
            "AND status = #{goodsStatus} " +
            "</if>" +
            "</script>")
    int countWithConditions(@Param("keyword") String keyword,
                          @Param("goodsCategory") String goodsCategory,
                          @Param("goodsStatus") String goodsStatus);
} 