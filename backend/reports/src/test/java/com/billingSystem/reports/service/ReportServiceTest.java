package com.billingSystem.reports.service;

import com.billingSystem.reports.entities.Product;
import com.billingSystem.reports.entities.SaleItem;
import com.billingSystem.reports.entities.SaleReport;
import com.billingSystem.reports.entities.Sales;
import com.billingSystem.reports.repository.SaleItemRepository;
import com.billingSystem.reports.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ReportServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private ReportService reportService;

    @Mock
    SaleItemRepository saleItemRepository;


    public ReportServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReport() {
        UUID saleId = UUID.randomUUID();
        Sales sale = new Sales();
        sale.setId(saleId);
        sale.setSaleDate(LocalDateTime.of(2025, 11, 2, 0, 0));
        sale.setTotalAmount(new BigDecimal("1000"));

        when(saleRepository.findBySaleDateBetween(
                LocalDate.of(2025, 11, 1).atStartOfDay(),
                LocalDate.of(2025, 11, 5).plusDays(1).atStartOfDay()))
                .thenReturn(List.of(sale));

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Product A");

        SaleItem saleItem = new SaleItem();
        saleItem.setProduct(product);
        saleItem.setQuantity(5);
        saleItem.setSale(sale);

        when(saleItemRepository.findBySaleId(saleId)).thenReturn(List.of(saleItem));

        List<SaleReport> reports = reportService.getReport(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 5));

        assertEquals(1, reports.size());
        assertEquals(saleId, reports.get(0).getSaleId());
        assertEquals(new BigDecimal("1000"), reports.get(0).getTotalAmt());
        assertEquals(1, reports.get(0).getSaleProducts().size());
        assertEquals("Product A", reports.get(0).getSaleProducts().get(0).getProduct().getName());
        assertEquals(5, reports.get(0).getSaleProducts().get(0).getSaleQuantity());
    }
}
