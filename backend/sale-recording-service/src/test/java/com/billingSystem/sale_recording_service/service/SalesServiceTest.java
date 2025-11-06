package com.billingSystem.sale_recording_service.service;

import com.billingSystem.sale_recording_service.entity.*;
import com.billingSystem.sale_recording_service.repository.CustomerRepository;
import com.billingSystem.sale_recording_service.repository.SaleRepository;
import com.billingSystem.sale_recording_service.repository.SalesItemRepository;
import com.billingSystem.sale_recording_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalesServiceTest {

    @Mock
    SaleRepository saleRepository;

    @Mock
    SalesItemRepository salesItemRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    UserRepository userRepository;


    @Mock
    InventoryService inventoryService;

    @InjectMocks
    SalesService salesService;

    @Test
    public void testRecordSaleWithNoDiscount() {
        SaleRequest request = new SaleRequest();
        request.setUserId(UUID.randomUUID());
        request.setCustomerId(UUID.randomUUID());
        request.setPaymentMethod("Cash");

        SaleRequest.SaleItemDto itemDto = new SaleRequest.SaleItemDto();
        itemDto.setProductId(UUID.randomUUID());
        itemDto.setQuantity(1);
        itemDto.setUnitPrice(new BigDecimal("700"));

        request.setItems(List.of(itemDto));

        Product product = new Product();
        product.setId(itemDto.getProductId());
        product.setQuantityOnHand(50);

        when(inventoryService.getProductById(itemDto.getProductId())).thenReturn(product);

        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customers()));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        Sale sale = salesService.recordSale(request);

        assertNotNull(sale);
        assertEquals(new BigDecimal("749.00"), sale.getTotalAmount());
        verify(inventoryService).updateStock(itemDto.getProductId(), 1);
    }

    @Test
    public void testRecordSaleWithDiscount() {
        SaleRequest request = new SaleRequest();
        request.setUserId(UUID.randomUUID());
        request.setCustomerId(UUID.randomUUID());
        request.setPaymentMethod("Card");

        SaleRequest.SaleItemDto itemDto = new SaleRequest.SaleItemDto();
        itemDto.setProductId(UUID.randomUUID());
        itemDto.setQuantity(3);
        itemDto.setUnitPrice(new BigDecimal("400"));

        request.setItems(List.of(itemDto));

        Product product = new Product();
        product.setId(itemDto.getProductId());
        product.setQuantityOnHand(50);

        when(inventoryService.getProductById(itemDto.getProductId())).thenReturn(product);
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customers()));
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        Sale sale = salesService.recordSale(request);

        BigDecimal subtotal = itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
        BigDecimal discount = subtotal.multiply(new BigDecimal("0.05"));
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = taxable.multiply(new BigDecimal("0.07"));
        BigDecimal expectedTotal = taxable.add(tax);

        assertNotNull(sale);
        assertEquals(0, sale.getTotalAmount().compareTo(expectedTotal));
        verify(inventoryService).updateStock(itemDto.getProductId(), 3);
    }

    @Test
    public void testGetSaleByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(saleRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> salesService.getSaleById(id));
        assertEquals("Sale not found", ex.getMessage());
    }
}
