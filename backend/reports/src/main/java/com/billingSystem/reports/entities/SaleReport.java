package com.billingSystem.reports.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SaleReport {
    private UUID saleId;
    private BigDecimal totalAmt;
    private LocalDateTime saleDate;
    private List<SaleProduct> saleProducts;
}