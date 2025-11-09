package com.billingSystem.reports.repository;

import com.billingSystem.reports.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SaleItemRepository extends JpaRepository<SaleItem, UUID> {
    List<SaleItem> findBySaleId(UUID saleId);
}

