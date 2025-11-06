package com.billingSystem.sale_recording_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue
    private UUID id;


    @JoinColumn(name = "customer_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Customers customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime saleDate;

    private BigDecimal totalAmount;

    private String paymentMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SaleItem> saleItem;
}
