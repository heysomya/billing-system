package com.billingSystem.stock_management_service.service;

import com.billingSystem.stock_management_service.entity.InventoryLog;
import com.billingSystem.stock_management_service.entity.Product;
import com.billingSystem.stock_management_service.repository.ProductRepository;
import com.billingSystem.stock_management_service.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {

    private ProductRepository productRepository;
    private StockRepository stockRepository;
    private StockService stockService;

    private UUID productId;
    private Product product;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        stockRepository = Mockito.mock(StockRepository.class);

        stockService = new StockService(productRepository, stockRepository);

        productId = UUID.randomUUID();
        product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setQuantityOnHand(20);
        product.setMinStockLevel(10);
    }

    @Test
    void testUpdateStock_Normal() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        String result = stockService.updateStock(productId, -5, "sold");

        assertEquals("Stock updated successfully", result);
        assertEquals(15, product.getQuantityOnHand());

        ArgumentCaptor<InventoryLog> logCaptor = ArgumentCaptor.forClass(InventoryLog.class);
        verify(stockRepository).save(logCaptor.capture());
        InventoryLog log = logCaptor.getValue();
        assertEquals(-5, log.getQuantityChange());
        assertEquals("sold", log.getReason());
        assertEquals(product, log.getProduct());

    }

    @Test
    void testUpdateStock_LowStock() {
        product.setQuantityOnHand(11);
        product.setMinStockLevel(10);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        stockService.updateStock(productId, -2, "sold");

        assertEquals(9, product.getQuantityOnHand());
    }

    @Test
    void testUpdateStock_ProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> stockService.updateStock(productId, 1, "reason"));
        verify(productRepository, never()).save(any());
    }

    @Test
    void testGetQuantity() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        product.setQuantityOnHand(17);
        Integer qty = stockService.getQuantity(productId);
        assertEquals(17, qty);
    }

    @Test
    void testGetQuantity_NotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> stockService.getQuantity(productId));
    }

    @Test
    void testGetInventoryLogs() {
        UUID productId = UUID.randomUUID();
        InventoryLog log1 = new InventoryLog();
        InventoryLog log2 = new InventoryLog();
        List<InventoryLog> logs = Arrays.asList(log1, log2);

        when(stockRepository.findByProductId(productId)).thenReturn(logs);

        List<InventoryLog> result = stockService.getInventoryLogs(productId);

        assertEquals(2, result.size());
        assertSame(log1, result.get(0));
        assertSame(log2, result.get(1));
        verify(stockRepository, times(1)).findByProductId(productId);
    }
}
