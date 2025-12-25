package com.logistics.mapper;

import com.logistics.entity.ShippingRate;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.math.BigDecimal;

@Mapper
public interface ShippingRateMapper {
    
    @Select("SELECT * FROM shipping_rates")
    List<ShippingRate> findAll();
    
    @Select("SELECT * FROM shipping_rates WHERE id = #{id}")
    ShippingRate findById(Integer id);
    
    @Select("SELECT * FROM shipping_rates WHERE origin_city = #{originCity}")
    List<ShippingRate> findByOriginCity(String originCity);
    
    @Select("SELECT * FROM shipping_rates WHERE destination_city = #{destinationCity}")
    List<ShippingRate> findByDestinationCity(String destinationCity);
    
    @Select("SELECT * FROM shipping_rates WHERE origin_city LIKE CONCAT('%', #{keyword}, '%') " +
            "OR destination_city LIKE CONCAT('%', #{keyword}, '%') " +
            "OR transport_type LIKE CONCAT('%', #{keyword}, '%')")
    List<ShippingRate> searchByKeyword(String keyword);
    
    @Select("SELECT * FROM shipping_rates WHERE origin_city = #{originCity} AND destination_city = #{destinationCity}")
    List<ShippingRate> findByRoute(@Param("originCity") String originCity, @Param("destinationCity") String destinationCity);
    
    @Select("SELECT * FROM shipping_rates WHERE transport_type = #{transportType}")
    List<ShippingRate> findByTransportType(String transportType);
    
    @Select("SELECT * FROM shipping_rates WHERE status = #{status}")
    List<ShippingRate> findByStatus(String status);
    
    @Select("SELECT * FROM shipping_rates WHERE status = 'active' " +
            "AND (effective_date IS NULL OR effective_date <= CURDATE()) " +
            "AND (expiry_date IS NULL OR expiry_date >= CURDATE())")
    List<ShippingRate> findActiveRates();
    
    @Select("SELECT * FROM shipping_rates WHERE origin_city = #{originCity} " +
            "AND destination_city = #{destinationCity} " +
            "AND transport_type = #{transportType} " +
            "AND status = 'active' " +
            "AND (effective_date IS NULL OR effective_date <= CURDATE()) " +
            "AND (expiry_date IS NULL OR expiry_date >= CURDATE()) " +
            "AND (weight_range_min IS NULL OR #{weight} >= weight_range_min) " +
            "AND (weight_range_max IS NULL OR #{weight} <= weight_range_max)")
    List<ShippingRate> findApplicableRates(@Param("originCity") String originCity, 
                                         @Param("destinationCity") String destinationCity,
                                         @Param("transportType") String transportType,
                                         @Param("weight") BigDecimal weight);
    
    @Insert("INSERT INTO shipping_rates (origin_city, destination_city, transport_type, " +
            "weight_range_min, weight_range_max, base_rate, rate_per_kg, rate_per_km, " +
            "fuel_surcharge_rate, effective_date, expiry_date, status) " +
            "VALUES (#{originCity}, #{destinationCity}, #{transportType}, " +
            "#{weightRangeMin}, #{weightRangeMax}, #{baseRate}, #{ratePerKg}, #{ratePerKm}, " +
            "#{fuelSurchargeRate}, #{effectiveDate}, #{expiryDate}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ShippingRate shippingRate);
    
    @Update("UPDATE shipping_rates SET origin_city = #{originCity}, destination_city = #{destinationCity}, " +
            "transport_type = #{transportType}, weight_range_min = #{weightRangeMin}, " +
            "weight_range_max = #{weightRangeMax}, base_rate = #{baseRate}, rate_per_kg = #{ratePerKg}, " +
            "rate_per_km = #{ratePerKm}, fuel_surcharge_rate = #{fuelSurchargeRate}, " +
            "effective_date = #{effectiveDate}, expiry_date = #{expiryDate}, status = #{status}, " +
            "updated_at = NOW() WHERE id = #{id}")
    int update(ShippingRate shippingRate);
    
    @Update("UPDATE shipping_rates SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
    
    @Delete("DELETE FROM shipping_rates WHERE id = #{id}")
    int deleteById(Integer id);
    
    @Select("SELECT COUNT(*) FROM shipping_rates")
    int count();
    
    @Select("SELECT COUNT(*) FROM shipping_rates WHERE status = #{status}")
    int countByStatus(String status);
    
    @Select("SELECT DISTINCT origin_city FROM shipping_rates WHERE origin_city IS NOT NULL ORDER BY origin_city")
    List<String> findAllOriginCities();
    
    @Select("SELECT DISTINCT destination_city FROM shipping_rates WHERE destination_city IS NOT NULL ORDER BY destination_city")
    List<String> findAllDestinationCities();
} 