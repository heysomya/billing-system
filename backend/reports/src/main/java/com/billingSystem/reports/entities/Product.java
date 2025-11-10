package com.billingSystem.reports.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
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
    private String category;

    private Double costPrice;

    private Double sellingPrice;

    private Integer quantityOnHand;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;


    public Product() {
    }
}

