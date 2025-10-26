package com.billingSystem.product_catalog_service.controller;

import com.billingSystem.product_catalog_service.entity.Product;
import com.billingSystem.product_catalog_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.create(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    @GetMapping("get/all")
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.update(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/name")
    public List<Product> searchProductsByName(@RequestParam String name) {
        return productService.searchByName(name);
    }

    @GetMapping("/search/category")
    public List<Product> searchProductsByCategory(@RequestParam String category) {
        return productService.searchByCategory(category);
    }

    @GetMapping("/search/SKU")
    public Product searchProductsBySKU(@RequestParam String category) {
        return productService.searchBySKU(category);
    }
}

