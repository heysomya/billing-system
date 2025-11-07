package com.billingSystem.product_catalog_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String sku;
    private UUID supplierId;

    private Integer minStockLevel;

    private String description;

    private Double costPrice;

    private Double sellingPrice;

    private Integer quantityOnHand;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;


    public Product() {
    }

    public Product(UUID id, String name, String sku, UUID supplierId, Integer minStockLevel, String description, Double costPrice, Double sellingPrice, Integer quantityOnHand, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.supplierId = supplierId;
        this.minStockLevel = minStockLevel;
        this.description = description;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.quantityOnHand = quantityOnHand;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
