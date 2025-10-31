package com.billingSystem.stock_management_service.repository;

import com.billingSystem.stock_management_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockQuantityLessThan(int minStockLevel);
}

