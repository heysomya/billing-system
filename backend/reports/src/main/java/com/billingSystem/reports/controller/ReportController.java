package com.billingSystem.reports.controller;

import com.billingSystem.reports.entities.SalesDetails;
import com.billingSystem.reports.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/daily")
    public List<SalesDetails> getDailyReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date is empty");
        }
        return reportService.getDailyReport(date);
    }

    @GetMapping("/weekly")
    public List<SalesDetails> getWeeklyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Date is empty");
        }
        return reportService.getWeeklyReport(startDate, endDate);
    }

    @GetMapping("/monthly")
    public List<SalesDetails> getMonthlyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Date is empty");
        }
        return reportService.getMonthlyReport(startDate, endDate);
    }


}

