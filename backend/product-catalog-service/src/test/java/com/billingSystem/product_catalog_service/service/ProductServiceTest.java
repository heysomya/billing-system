package com.billingSystem.product_catalog_service.service;

import com.billingSystem.product_catalog_service.entity.InventoryLog;
import com.billingSystem.product_catalog_service.entity.Product;
import com.billingSystem.product_catalog_service.repository.ProductRepository;
import com.billingSystem.product_catalog_service.repository.StockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockRepository stockRepository;


    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product updatedProduct;

    UUID id1 = UUID.fromString("9b180abb-63ee-4bf8-9404-35c1e2316881");
    UUID id2 = UUID.fromString("e0514e45-bd50-4e2d-bb99-2c77d90d7dbc");



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product1 = new Product(
                id1,
                "Logitech Wireless Mouse",
                "SKU1001",
                UUID.fromString("e4a1a8a2-374d-45f6-8e25-bad4721688a2"),
                10,
                "Ergonomic 2.4GHz wireless Logitech Wireless Mouse with nano receiver",
                "Electronics",
                15.90,
                21.99,
                120,
                OffsetDateTime.parse("2025-11-01T10:00:00Z"),
                OffsetDateTime.parse("2025-11-02T12:30:00Z")
        );

        updatedProduct = new Product(
                id2,
                "Mechanical Gaming Keyboard",
                "SKU1002",
                UUID.fromString("2fae7b3e-5d6d-424e-9e47-712f4b7df024"),
                5,
                "RGB mechanical keyboard with blue switches and wrist rest",
                "Electronics",
                49.99,
                69.99,
                60,
                OffsetDateTime.parse("2025-11-01T11:00:00Z"),
                OffsetDateTime.parse("2025-11-02T13:15:00Z")
        );
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(product1)).thenReturn(product1);

        Product created = productService.create(product1);

        Assertions.assertEquals("Logitech Wireless Mouse", created.getName());
        Assertions.assertEquals(15.9, created.getCostPrice());
    }

    @Test
    void testFindAllProducts() {
        List<Product> products = Arrays.asList(product1);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Logitech Wireless Mouse", result.get(0).getName());
    }

    @Test
    void testFindByIdFound() {
        when(productRepository.findById(id1)).thenReturn(Optional.of(product1));

        Optional<Product> result = productService.findById(id1);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Logitech Wireless Mouse", result.get().getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById(id2)).thenReturn(Optional.empty());
        Optional<Product> result = productService.findById(id2);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testUpdateProductSuccess() {
        when(productRepository.findById(id1)).thenReturn(Optional.of(product1));
        when(productRepository.save(Mockito.any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        Product result = productService.update(id1, updatedProduct);

        Assertions.assertEquals("Mechanical Gaming Keyboard", result.getName());
        Assertions.assertEquals(49.99, result.getCostPrice());
    }

    @Test
    void testUpdateProductNotFound() {
        when(productRepository.findById(id2)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.update(id2, updatedProduct);
        });

        Assertions.assertTrue(exception.getMessage().contains("Product not found"));
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product1));
        productService.delete(id1);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(id1);
    }
    @Test
    void testSearchByName() {
        when(productRepository.findByName("Logitech Wireless Mouse")).thenReturn(product1);

        Product result = productService.searchByName("Logitech Wireless Mouse");

        Assertions.assertEquals("Logitech Wireless Mouse", result.getName());
    }

    @Test
    void testSearchBySKU() {
        when(productRepository.findBySku("SKU1001")).thenReturn(product1);

        Product result = productService.searchBySKU("SKU1001");

        Assertions.assertEquals("SKU1001", result.getSku());
        Assertions.assertEquals("Logitech Wireless Mouse", result.getName());
    }

    @Test
    void testSearchByCategory() {
        when(productRepository.findByCategory("Electronics")).thenReturn(product1);

        Product result = productService.searchByCategory("Electronics");

        Assertions.assertEquals("Electronics", result.getCategory());
        Assertions.assertEquals("Logitech Wireless Mouse", result.getName());
    }
}
