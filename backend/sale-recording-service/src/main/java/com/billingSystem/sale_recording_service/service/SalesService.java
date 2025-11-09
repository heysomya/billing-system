package com.billingSystem.sale_recording_service.service;

import com.billingSystem.sale_recording_service.entity.*;
import com.billingSystem.sale_recording_service.repository.CustomerRepository;
import com.billingSystem.sale_recording_service.repository.SaleRepository;
import com.billingSystem.sale_recording_service.repository.SalesItemRepository;
import com.billingSystem.sale_recording_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SalesService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.07");
    private static final BigDecimal DISCOUNT_THRESHOLD = new BigDecimal("100.00");
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.05");

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SalesItemRepository salesItemRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public Sale recordSale(SaleRequest request) {
        Sale sale = new Sale();
        Customers customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        sale.setCustomer(customer);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        sale.setUser(user);

        sale.setSaleDate(LocalDateTime.now());
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setCreatedAt(LocalDateTime.now());
        sale.setUpdatedAt(LocalDateTime.now());

        List<SaleItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (SaleRequest.SaleItemDto dto : request.getItems()) {
            Product product = inventoryService.getProductById(dto.getProductId());
            SaleItem item = new SaleItem();
            item.setSale(sale);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());

            inventoryService.updateStock(dto.getProductId(), dto.getQuantity());

            subtotal = subtotal.add(dto.getUnitPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
            items.add(item);
        }

        BigDecimal discount = BigDecimal.ZERO;
        if (subtotal.compareTo(DISCOUNT_THRESHOLD) >= 0) {
            discount = subtotal.multiply(DISCOUNT_RATE);
        }

        BigDecimal taxableAmount = subtotal.subtract(discount);
        BigDecimal tax = taxableAmount.multiply(TAX_RATE);
        BigDecimal totalAmount = taxableAmount.add(tax);

        sale.setTotalAmount(totalAmount);
        sale.setSaleItem(items);

        saleRepository.save(sale);
        items.forEach(salesItemRepository::save);

        return sale;
    }

    public Sale getSaleById(UUID id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
    }
}

