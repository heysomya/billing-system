package com.billingSystem.sale_recording_service.repository;

import com.billingSystem.sale_recording_service.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
}
