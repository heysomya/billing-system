package com.billingSystem.product_catalog_service.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
public class InventoryLog {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantityChange;
    private String reason;
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
