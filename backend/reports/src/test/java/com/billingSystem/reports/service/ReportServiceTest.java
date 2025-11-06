package com.billingSystem.reports.service;

import com.billingSystem.reports.entities.SalesDetails;
import com.billingSystem.reports.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReportServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private ReportService reportService;

    public ReportServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetDailyReport() {
        SalesDetails mockSummary = new SalesDetails(
                LocalDateTime.of(2025, 10, 30, 0, 0),
                LocalDateTime.of(2025, 10, 30, 23, 59),
                new BigDecimal("5000"),
                10L);

        when(saleRepository.findDailySalesSummary(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(mockSummary));

        List<SalesDetails> result = reportService.getDailyReport(LocalDate.of(2025, 10, 30));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("5000"), result.getFirst().getTotalSalesAmount());
    }

    @Test
    public void testGetWeeklyReport() {
        SalesDetails mockSummary = new SalesDetails(
                LocalDateTime.of(2025, 10, 25, 0, 0),
                LocalDateTime.of(2025, 10, 31, 23, 59),
                new BigDecimal("50000"),
                70L);

        when(saleRepository.findWeeklySalesSummary(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(mockSummary));

        List<SalesDetails> result = reportService.getWeeklyReport(
                LocalDate.of(2025, 10, 25),
                LocalDate.of(2025, 10, 31));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("50000"), result.getFirst().getTotalSalesAmount());
    }

    @Test
    public void testGetMonthlyReport() {
        SalesDetails mockSummary = new SalesDetails(
                LocalDateTime.of(2025, 10, 1, 0, 0),
                LocalDateTime.of(2025, 10, 31, 23, 59),
                new BigDecimal("100000"),
                300L);

        when(saleRepository.findMonthlySalesSummary(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(mockSummary));

        List<SalesDetails> result = reportService.getMonthlyReport(
                LocalDate.of(2025, 10, 1),
                LocalDate.of(2025, 10, 31));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("100000"), result.getFirst().getTotalSalesAmount());
    }
}
