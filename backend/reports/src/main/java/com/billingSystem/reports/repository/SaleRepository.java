package com.billingSystem.reports.repository;

import com.billingSystem.reports.entities.Sales;
import com.billingSystem.reports.entities.SalesDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sales, UUID> {
    List<Sales> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
