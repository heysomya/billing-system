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

    @Query("SELECT new com.billingSystem.reports.entities.SalesDetails(" +
            "MIN(s.saleDate), MAX(s.saleDate), SUM(s.totalAmount), COUNT(s)) " +
            "FROM Sales s " +
            "WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('date', s.saleDate)")
    List<SalesDetails> findDailySalesSummary(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.billingSystem.reports.entities.SalesDetails(" +
            "MIN(s.saleDate), MAX(s.saleDate), SUM(s.totalAmount), COUNT(s)) " +
            "FROM Sales s " +
            "WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY EXTRACT(YEAR FROM s.saleDate), EXTRACT(WEEK FROM s.saleDate)")
    List<SalesDetails> findWeeklySalesSummary(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.billingSystem.reports.entities.SalesDetails(" +
            "MIN(s.saleDate), MAX(s.saleDate), SUM(s.totalAmount), COUNT(s)) " +
            "FROM Sales s " +
            "WHERE s.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY EXTRACT(YEAR FROM s.saleDate), EXTRACT(MONTH FROM s.saleDate)")
    List<SalesDetails> findMonthlySalesSummary(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);
}
