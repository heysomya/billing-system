package com.billingSystem.reports.controller;

import com.billingSystem.reports.entities.SalesDetails;
import com.billingSystem.reports.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReportControllerTest {

    @Test
    public void testDailyReport() {
        ReportService reportService = Mockito.mock(ReportService.class);
        ReportController controller = new ReportController();
        controller.reportService = reportService;

        SalesDetails summary = new SalesDetails(
                LocalDateTime.of(2025, 10, 15, 0, 0),
                LocalDateTime.of(2025, 10, 15, 23, 59),
                new BigDecimal("500"),
                10L);

        Mockito.when(reportService.getDailyReport(LocalDate.of(2025, 10, 30)))
                .thenReturn(List.of(summary));

        var response = controller.getDailyReport(LocalDate.of(2025, 10, 30));

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(new BigDecimal("500"), response.getFirst().getTotalSalesAmount());
    }

    @Test
    public void testWeeklyReport() {
        ReportService reportService = Mockito.mock(ReportService.class);
        ReportController controller = new ReportController();
        controller.reportService = reportService;

        SalesDetails summary = new SalesDetails(
                LocalDateTime.of(2025, 10, 25, 0, 0),
                LocalDateTime.of(2025, 10, 31, 23, 59),
                new BigDecimal("3500"),
                70L);

        Mockito.when(reportService.getWeeklyReport(LocalDate.of(2025, 10, 25), LocalDate.of(2025, 10, 31)))
                .thenReturn(List.of(summary));

        var response = controller.getWeeklyReport(LocalDate.of(2025, 10, 25), LocalDate.of(2025, 10, 31));

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(new BigDecimal("3500"), response.getFirst().getTotalSalesAmount());
    }

    @Test
    public void testMonthlyReport() {
        ReportService reportService = Mockito.mock(ReportService.class);
        ReportController controller = new ReportController();
        controller.reportService = reportService;

        SalesDetails summary = new SalesDetails(
                LocalDateTime.of(2025, 10, 1, 0, 0),
                LocalDateTime.of(2025, 10, 31, 23, 59),
                new BigDecimal("15000"),
                300L);

        Mockito.when(reportService.getMonthlyReport(LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31)))
                .thenReturn(List.of(summary));

        var response = controller.getMonthlyReport(LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31));

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(new BigDecimal("15000"), response.getFirst().getTotalSalesAmount());
    }

    @Test
    public void testDailyReportNullDate() {
        ReportController controller = new ReportController();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            controller.getDailyReport(null);
        });
        assertEquals("Date is empty", thrown.getMessage());
    }

    @Test
    public void testWeeklyReportNullDates() {
        ReportController controller = new ReportController();

        IllegalArgumentException thrown1 = assertThrows(IllegalArgumentException.class, () -> {
            controller.getWeeklyReport(null, LocalDate.now());
        });
        assertEquals("Date is empty", thrown1.getMessage());

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> {
            controller.getWeeklyReport(LocalDate.now(), null);
        });
        assertEquals("Date is empty", thrown2.getMessage());
    }

    @Test
    public void testMonthlyReportNullDates() {
        ReportController controller = new ReportController();

        IllegalArgumentException thrown1 = assertThrows(IllegalArgumentException.class, () -> {
            controller.getMonthlyReport(null, LocalDate.now());
        });
        assertEquals("Date is empty", thrown1.getMessage());

        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> {
            controller.getMonthlyReport(LocalDate.now(), null);
        });
        assertEquals("Date is empty", thrown2.getMessage());
    }
}
