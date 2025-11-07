package com.billingSystem.product_catalog_service.service;

import com.billingSystem.product_catalog_service.entity.InventoryLog;
import com.billingSystem.product_catalog_service.entity.Product;
import com.billingSystem.product_catalog_service.repository.ProductRepository;
import com.billingSystem.product_catalog_service.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;


    public Product create(Product product) {
        product.setCreatedAt(OffsetDateTime.now());
        product.setUpdatedAt(OffsetDateTime.now());
        Product savedProduct = productRepository.save(product);
        saveLogs(savedProduct, "Initial stock");
        return savedProduct;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(UUID id) {
        return productRepository.findById(id);
    }

    public Product update(UUID id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setCostPrice(updatedProduct.getCostPrice());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setSku(updatedProduct.getSku());
                    existingProduct.setQuantityOnHand(updatedProduct.getQuantityOnHand());
                    existingProduct.setSellingPrice(updatedProduct.getSellingPrice());
                    existingProduct.setMinStockLevel(updatedProduct.getMinStockLevel());
                    existingProduct.setUpdatedAt(OffsetDateTime.now());
                    Product savedProduct = productRepository.save(existingProduct);
                    saveLogs(savedProduct, "updated stock");
                    return savedProduct;
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public void delete(UUID id) {
        Optional<Product> products = productRepository.findById(id);
        if (products.isPresent()) {
            saveLogs(products.get(), "product deleted");
        }
        else{
            throw new RuntimeException("Product not found with " + id);
        }
        productRepository.deleteById(id);
    }

    public Product searchByName(String name) {
        return productRepository.findByName(name);
    }

    public Product searchBySKU(String name) {
        return productRepository.findBySku(name);
    }

    private void saveLogs(Product product, String reason) {
            InventoryLog log = new InventoryLog();
            log.setProduct(product);
            log.setQuantityChange(product.getQuantityOnHand());
            log.setReason(reason);
            log.setCreatedAt(Instant.now());
            stockRepository.save(log);
    }
}

