package com.billingSystem.stock_management_service.service;

import com.billingSystem.stock_management_service.entity.InventoryLog;
import com.billingSystem.stock_management_service.entity.Product;
import com.billingSystem.stock_management_service.repository.ProductRepository;
import com.billingSystem.stock_management_service.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private EmailService emailService;

    public StockService(ProductRepository productRepository, StockRepository stockRepository, EmailService emailService) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.emailService = emailService;
    }

    public String updateStock(UUID productId, int change, String reason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
        product.setQuantityOnHand(product.getQuantityOnHand() + change);
        productRepository.save(product);

        InventoryLog log = new InventoryLog();
        log.setProduct(product);
        log.setQuantityChange(change);
        log.setReason(reason);
        log.setCreatedAt(Instant.now());
        stockRepository.save(log);

//        if (product.getQuantityOnHand() < product.getMinStockLevel()) {
//            emailService.sendStockAlert("email", product.getName(), product.getQuantityOnHand());
//            return "Warning: Stock is low for product " + product.getName();
//        }
        return "Stock updated successfully";
    }

    public List<InventoryLog> getInventoryLogs(UUID productId) {
        return stockRepository.findByProductId(productId);
    }

    public Integer getQuantity(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId))
                .getQuantityOnHand();
    }


}