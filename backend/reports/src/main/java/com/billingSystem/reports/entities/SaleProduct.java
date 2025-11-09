package com.billingSystem.reports.entities;



import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class SaleProduct {
    private Product product;
    private int saleQuantity;
}
