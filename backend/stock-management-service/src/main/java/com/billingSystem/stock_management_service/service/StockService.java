package com.billingSystem.stock_management_service.service;

import com.billingSystem.stock_management_service.entity.Product;
import com.billingSystem.stock_management_service.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final JavaMailSender mailSender;

    public Product addProduct(Product p) { return stockRepository.save(p); }

    public Product updateStock(Long id, int newQty) {
        Product p = stockRepository.findById(id).orElseThrow();
        p.setStockQuantity(newQty);
        return stockRepository.save(p);
    }

    public Product getProduct(Long id) { return stockRepository.findById(id).orElseThrow(); }

    public List<Product> getAllLowStock() {
        return stockRepository.findByStockQuantityLessThan(5);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void checkAndAlertLowStock() {
        List<Product> lowStock = getAllLowStock();
        for (Product p : lowStock) {
            sendLowStockAlert(p);
        }
    }

    private void sendLowStockAlert(Product product) {
        if (product.getEmailForAlert() == null || product.getEmailForAlert().isEmpty()) return;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(product.getEmailForAlert());
        message.setSubject("Low Stock Alert: " + product.getName());
        message.setText("Stock for " + product.getName() +
                " is low: " + product.getStockQuantity());
        mailSender.send(message);
    }
}
