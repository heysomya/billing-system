package com.billingSystem.product_catalog_service.repository;

import com.billingSystem.product_catalog_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByCategory(String category);
    Product findBySku(String sku);
}

