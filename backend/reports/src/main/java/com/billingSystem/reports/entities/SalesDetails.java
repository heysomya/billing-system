package com.billingSystem.reports.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
public class SalesDetails {
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private BigDecimal totalSalesAmount;
    private Long transactionCount;

    private BigDecimal averageSaleValue;

    public SalesDetails(LocalDateTime start, LocalDateTime end, BigDecimal totalSales, Long transactions) {
        this.periodStart = start;
        this.periodEnd = end;
        this.totalSalesAmount = totalSales;
        this.transactionCount = transactions;
        this.averageSaleValue = transactions > 0 ?
                totalSales.divide(BigDecimal.valueOf(transactions), RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
}

