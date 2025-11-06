package com.billingSystem.sale_recording_service.repository;

import com.billingSystem.sale_recording_service.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalesItemRepository extends JpaRepository<SaleItem, UUID> {
}
