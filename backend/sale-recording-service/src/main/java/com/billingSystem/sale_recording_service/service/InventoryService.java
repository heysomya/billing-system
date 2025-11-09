package com.billingSystem.sale_recording_service.service;

import com.billingSystem.sale_recording_service.entity.InventoryLog;
import com.billingSystem.sale_recording_service.entity.Product;
import com.billingSystem.sale_recording_service.repository.InventoryRepository;
import com.billingSystem.sale_recording_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class InventoryService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryRepository inventoryRepository;


    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public void updateStock(UUID productId, int quantitySold) {
        Product product = getProductById(productId);
        int newQty = product.getQuantityOnHand() - quantitySold;
        if (newQty < 0) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        product.setQuantityOnHand(newQty);
        productRepository.save(product);

        InventoryLog log = new InventoryLog();
        log.setProduct(product);
        log.setQuantityChange(-quantitySold);
        log.setReason("Product Sold");
        log.setCreatedAt(Instant.now());
        inventoryRepository.save(log);

    }
}

