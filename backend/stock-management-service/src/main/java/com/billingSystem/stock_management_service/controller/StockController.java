package com.billingSystem.stock_management_service.controller;

import com.billingSystem.stock_management_service.entity.Product;
import com.billingSystem.stock_management_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PostMapping("/add")
    public Product addProduct(@RequestBody Product p) { return stockService.addProduct(p); }

    @PutMapping("/update/{id}")
    public Product updateStock(@PathVariable Long id, @RequestParam int newQty) {
        return stockService.updateStock(id, newQty);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) { return stockService.getProduct(id); }

    @GetMapping("/low")
    public List<Product> getAllLowStock() { return stockService.getAllLowStock(); }
}

