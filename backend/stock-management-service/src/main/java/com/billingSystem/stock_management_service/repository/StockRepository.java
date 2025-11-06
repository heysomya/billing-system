package com.billingSystem.stock_management_service.repository;

import com.billingSystem.stock_management_service.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockRepository extends JpaRepository<InventoryLog, UUID> {
    List<InventoryLog> findByProductId(UUID productId);
}

