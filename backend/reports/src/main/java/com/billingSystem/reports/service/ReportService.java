package com.billingSystem.reports.service;

import com.billingSystem.reports.entities.*;
import com.billingSystem.reports.repository.SaleItemRepository;
import com.billingSystem.reports.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    public List<SaleReport> getReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        List<Sales> salesList = saleRepository.findBySaleDateBetween(startDateTime, endDateTime);
        List<SaleReport> reports = new ArrayList<>();

        for (Sales sale : salesList) {
            SaleReport report = new SaleReport();
            report.setSaleId(sale.getId());
            report.setTotalAmt(sale.getTotalAmount());
            report.setSaleDate(sale.getSaleDate());

            List<SaleItem> saleItems = saleItemRepository.findBySaleId(sale.getId());
            List<SaleProduct> saleProducts = new ArrayList<>();
            for (SaleItem item : saleItems) {
                SaleProduct saleProduct = new SaleProduct();
                saleProduct.setProduct(item.getProduct());
                saleProduct.setSaleQuantity(item.getQuantity());
                saleProducts.add(saleProduct);
            }
            report.setSaleProducts(saleProducts);

            reports.add(report);
        }
        return reports;
    }
}
