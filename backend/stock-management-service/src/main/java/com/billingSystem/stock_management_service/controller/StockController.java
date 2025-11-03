package com.billingSystem.stock_management_service.controller;

import com.billingSystem.stock_management_service.entity.InventoryLog;
import com.billingSystem.stock_management_service.entity.Product;
import com.billingSystem.stock_management_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateStock(@PathVariable UUID id, @RequestParam int updatedQuantity, @RequestParam String reason) {
        return ResponseEntity.ok(stockService.updateStock(id, updatedQuantity, reason));
    }

    @GetMapping("/logs/{productId}")
    public List<InventoryLog> getLogsForProduct(@PathVariable UUID productId) {
        return stockService.getInventoryLogs(productId);
    }

    @GetMapping("/{productId}")
    public Integer getStock(@PathVariable UUID productId) {
        return stockService.getQuantity(productId);
    }
}

