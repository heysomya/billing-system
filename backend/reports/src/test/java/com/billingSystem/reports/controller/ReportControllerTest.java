package com.billingSystem.reports.controller;

import com.billingSystem.reports.entities.SaleReport;
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
    public void testGetReports() {
        ReportService reportServices = Mockito.mock(ReportService.class);
        ReportController controller = new ReportController();
        controller.reportService = reportServices;

        SaleReport saleReport = new SaleReport();
        saleReport.setSaleDate(LocalDateTime.of(2025, 11, 2, 0, 0));
        saleReport.setTotalAmt(new BigDecimal("5000"));

        Mockito.when(reportServices.getReport(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 5)))
                .thenReturn(List.of(saleReport));

        List<SaleReport> result = controller.getReports(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 5));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("5000"), result.get(0).getTotalAmt());
    }

}
