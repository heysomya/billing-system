package com.billingSystem.sale_recording_service.entity;

import lombok.*;

import java.util.List;
import java.util.UUID;


import java.math.BigDecimal;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
    private UUID customerId;
    private UUID userId;
    private String paymentMethod;
    private List<SaleItemDto> items;

    @Getter
    @Setter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaleItemDto {
        private UUID productId;
        private int quantity;
        private BigDecimal unitPrice;
    }
}

