package com.logistics.service;

import com.logistics.entity.Inventory;
import com.logistics.mapper.InventoryMapper;
import com.logistics.common.PageRequest;
import com.logistics.common.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryMapper inventoryMapper;
    
    public List<Inventory> findAll() {
        return inventoryMapper.findAll();
    }
    
    /**
     * 分页查询库存
     */
    public PageResult<Inventory> findByPage(PageRequest pageRequest) {
        List<Inventory> inventories = inventoryMapper.findAll();
        return new PageResult<>(inventories, inventories.size(), 1, inventories.size());
    }
    
    public Inventory findById(Integer id) {
        return inventoryMapper.findById(id);
    }
    
    public Inventory findById(Long id) {
        return inventoryMapper.findById(id.intValue());
    }
    
    public List<Inventory> findByWarehouseId(Long warehouseId) {
        return inventoryMapper.findByWarehouseId(warehouseId.intValue());
    }
    
    public List<Inventory> findByGoodsId(Long goodsId) {
        return inventoryMapper.findByGoodsId(goodsId.intValue());
    }
    
    /**
     * 根据关键词搜索库存
     */
    public List<Inventory> searchByKeyword(String keyword) {
        return inventoryMapper.searchByKeyword(keyword);
    }
    
    public List<Inventory> findByLocation(String location) {
        return inventoryMapper.findByLocation(location);
    }
    
    public List<Inventory> findByBatchNumber(String batchNumber) {
        return inventoryMapper.findByBatchNumber(batchNumber);
    }
    
    public List<Inventory> findLowStockItems(Integer threshold) {
        return inventoryMapper.findLowStockItems(threshold);
    }
    
    public List<Inventory> findExpiredItems() {
        return inventoryMapper.findExpiredItems();
    }
    
    public List<Inventory> findExpiringItems(Integer days) {
        return inventoryMapper.findExpiringItems(days);
    }
    
    public boolean save(Inventory inventory) {
        try {
            if (inventory.getId() == null) {
                // 新增
                if (inventory.getReservedQuantity() == null) {
                    inventory.setReservedQuantity(0);
                }
                if (inventory.getSafetyStock() == null) {
                    inventory.setSafetyStock(10);
                }
                inventoryMapper.insert(inventory);
            } else {
                // 更新
                inventoryMapper.update(inventory);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 库存入库
     */
    public boolean inbound(Inventory inventory) {
        try {
            return inventoryMapper.adjustQuantity(inventory.getId(), inventory.getActualQuantity()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 库存出库
     */
    public boolean outbound(Inventory inventory) {
        try {
            return inventoryMapper.adjustQuantity(inventory.getId(), -inventory.getActualQuantity()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 库存调整
     */
    public boolean adjust(Inventory inventory) {
        try {
            return inventoryMapper.adjustQuantity(inventory.getId(), inventory.getActualQuantity()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean adjustQuantity(Integer id, Integer adjustment) {
        return inventoryMapper.adjustQuantity(id, adjustment) > 0;
    }
    
    public boolean reserveStock(Integer id, Integer quantity) {
        return inventoryMapper.reserveStock(id, quantity) > 0;
    }
    
    public boolean releaseReservedStock(Integer id, Integer quantity) {
        return inventoryMapper.releaseReservedStock(id, quantity) > 0;
    }
    
    public boolean deleteById(Integer id) {
        return inventoryMapper.deleteById(id) > 0;
    }
    
    /**
     * 删除库存（支持Long类型）
     */
    public boolean delete(Long id) {
        try {
            return inventoryMapper.deleteById(id.intValue()) > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int count() {
        return inventoryMapper.count();
    }
    
    public int countByWarehouse(Integer warehouseId) {
        return inventoryMapper.countByWarehouse(warehouseId);
    }
    
    public int getTotalStock() {
        return inventoryMapper.getTotalStock();
    }
    
    public int getTotalAvailableStock() {
        return inventoryMapper.getTotalAvailableStock();
    }
    
    public int getTotalReservedStock() {
        return inventoryMapper.getTotalReservedStock();
    }
} 