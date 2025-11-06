package com.billingSystem.reports.service;

import com.billingSystem.reports.entities.SalesDetails;
import com.billingSystem.reports.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private SaleRepository saleRepository;


    public List<SalesDetails> getDailyReport(LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();
        return saleRepository.findDailySalesSummary(startDateTime, endDateTime);
    }

    public List<SalesDetails> getWeeklyReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        return saleRepository.findWeeklySalesSummary(startDateTime, endDateTime);
    }

    public List<SalesDetails> getMonthlyReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        return saleRepository.findMonthlySalesSummary(startDateTime, endDateTime);
    }
}

