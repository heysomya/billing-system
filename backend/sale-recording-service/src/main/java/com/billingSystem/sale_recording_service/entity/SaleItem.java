package com.billingSystem.sale_recording_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "sales_items")
public class SaleItem {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Sale sale;

    @ManyToOne
    private Product product;

    private int quantity;

    private BigDecimal unitPrice;
}
