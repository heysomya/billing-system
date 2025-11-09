package com.billingSystem.user_authentication.repository;

import com.billingSystem.user_authentication.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
