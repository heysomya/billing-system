package com.billingSystem.sale_recording_service.repository;

import com.billingSystem.sale_recording_service.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryLog, UUID> {
}

