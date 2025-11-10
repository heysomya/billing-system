package com.billingSystem.product_catalog_service.repository;

import com.billingSystem.product_catalog_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product findByName(String name);
    Product findBySku(String sku);
    Product findByCategory(String name);
}

