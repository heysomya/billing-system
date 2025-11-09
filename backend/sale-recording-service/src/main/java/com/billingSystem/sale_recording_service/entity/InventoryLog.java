package com.billingSystem.sale_recording_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
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

}
