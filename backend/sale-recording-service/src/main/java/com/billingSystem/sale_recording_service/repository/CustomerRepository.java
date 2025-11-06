package com.billingSystem.sale_recording_service.repository;

import com.billingSystem.sale_recording_service.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customers, UUID> {
}
