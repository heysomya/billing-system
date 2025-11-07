package com.billingSystem.stock_management_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String sku;
    private String description;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private Integer quantityOnHand;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer minStockLevel;

}

