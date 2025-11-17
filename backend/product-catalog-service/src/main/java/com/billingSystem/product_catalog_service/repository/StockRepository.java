package com.billingSystem.product_catalog_service.repository;

import com.billingSystem.product_catalog_service.entity.InventoryLog;
import com.billingSystem.product_catalog_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockRepository extends JpaRepository<InventoryLog, UUID> {
    List<InventoryLog> findByProductId(UUID productId);
    List<InventoryLog> deleteByProduct(Product product);

}

