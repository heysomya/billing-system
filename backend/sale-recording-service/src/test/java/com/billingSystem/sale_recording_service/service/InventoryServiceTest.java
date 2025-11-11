package com.billingSystem.sale_recording_service.service;


import com.billingSystem.sale_recording_service.entity.Product;
import com.billingSystem.sale_recording_service.repository.InventoryRepository;
import com.billingSystem.sale_recording_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Test
    public void testGetProductByIdFound() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = inventoryService.getProductById(productId);
        assertEquals(productId, result.getId());
    }

    @Test
    public void testGetProductById_NotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> inventoryService.getProductById(productId));
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testUpdateStockSuccess() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setQuantityOnHand(10);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        inventoryService.updateStock(productId, 3);

        assertEquals(7, product.getQuantityOnHand());
        verify(productRepository).save(product);

        verify(inventoryRepository).save(argThat(log ->
                log.getProduct().equals(product) &&
                        log.getQuantityChange() == -3 &&
                        "Product Sold".equals(log.getReason()) &&
                        log.getCreatedAt() != null
        ));
    }

    @Test
    public void testUpdateStockInsufficientStock() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setQuantityOnHand(2);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> inventoryService.updateStock(productId, 5));
        assertTrue(exception.getMessage().contains("Insufficient stock"));

        verify(productRepository, never()).save(any());
        verify(inventoryRepository, never()).save(any());

    }
}
