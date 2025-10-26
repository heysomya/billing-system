package com.billingSystem.product_catalog_service.service;

import com.billingSystem.product_catalog_service.entity.Product;
import com.billingSystem.product_catalog_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product update(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setSku(updatedProduct.getSku());
                    existingProduct.setQuantity(updatedProduct.getQuantity());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByName(name);
    }
    public List<Product> searchByCategory(String name) {
        return productRepository.findByCategory(name);
    }
    public Product searchBySKU(String name) {
        return productRepository.findBySku(name);
    }
}

