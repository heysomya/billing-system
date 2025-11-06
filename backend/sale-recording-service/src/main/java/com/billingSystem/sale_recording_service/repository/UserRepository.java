package com.billingSystem.sale_recording_service.repository;

import com.billingSystem.sale_recording_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
