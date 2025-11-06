package com.billingSystem.sale_recording_service.service;

import com.billingSystem.sale_recording_service.entity.Product;
import com.billingSystem.sale_recording_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryService {
    @Autowired
    private ProductRepository productRepository;

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
    }
}

