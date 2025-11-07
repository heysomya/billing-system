package com.billingSystem.product_catalog_service.controller;

import com.billingSystem.product_catalog_service.entity.Product;
import com.billingSystem.product_catalog_service.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;

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
                15.90,
                21.99,
                120,
                OffsetDateTime.parse("2025-11-01T10:00:00Z"),
                OffsetDateTime.parse("2025-11-02T12:30:00Z")
        );

         product2 = new Product(
                id2,
                "Mechanical Gaming Keyboard",
                "SKU1002",
                UUID.fromString("2fae7b3e-5d6d-424e-9e47-712f4b7df024"),
                5,
                "RGB mechanical keyboard with blue switches and wrist rest",
                49.99,
                69.99,
                60,
                OffsetDateTime.parse("2025-11-01T11:00:00Z"),
                OffsetDateTime.parse("2025-11-02T13:15:00Z")
        );
    }

    @Test
    void testGetProductById() {
        Mockito.when(productService.findById(id1)).thenReturn(Optional.of(product1));

        ResponseEntity<Product> response = productController.getProductById(id1);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("Logitech Wireless Mouse", response.getBody().getName());
    }

    @Test
    void testGetProductByIdNotFound() {
        Mockito.when(productService.findById(id2)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(id2);

        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void testCreateProduct() {
        Mockito.when(productService.create(product1)).thenReturn(product1);

        ResponseEntity<Product> response = productController.createProduct(product1);

        Assertions.assertEquals(201, response.getStatusCodeValue());
        Assertions.assertEquals("Logitech Wireless Mouse", response.getBody().getName());
    }

    @Test
    void testUpdateProduct() {
        Mockito.when(productService.update(id1, product1)).thenReturn(product1);

        ResponseEntity<Product> response = productController.updateProduct(id1, product1);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("Logitech Wireless Mouse", response.getBody().getName());
    }

    @Test
    void testUpdateProductNotFound() {
        Mockito.when(productService.update(id2, product1)).thenThrow(new RuntimeException("Product not found"));

        ResponseEntity<Product> response = productController.updateProduct(id2, product1);

        Assertions.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteProduct() {
        Mockito.doNothing().when(productService).delete(id1);

        ResponseEntity<Void> response = productController.deleteProduct(id1);

        Assertions.assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product1, product2);

        Mockito.when(productService.findAll()).thenReturn(products);

        List<Product> actualProducts = productController.getAllProducts();

        Assertions.assertEquals(2, actualProducts.size());
        Assertions.assertEquals("Logitech Wireless Mouse", actualProducts.get(0).getName());
        Assertions.assertEquals("Mechanical Gaming Keyboard", actualProducts.get(1).getName());
    }

    @Test
    void testSearchProductsByName() {

        Mockito.when(productService.searchByName("Logitech Wireless Mouse")).thenReturn(product1);

        Product actualProducts = productController.searchProductsByName("Logitech Wireless Mouse");

        Assertions.assertEquals("Logitech Wireless Mouse", actualProducts.getName());
    }
    @Test
    void testSearchProductsBySKU() {
        Mockito.when(productService.searchBySKU("SKU1001")).thenReturn(product1);

        Product actualProduct = productController.searchProductsBySKU("SKU1001");

        Assertions.assertNotNull(actualProduct);
        Assertions.assertEquals("SKU1001", actualProduct.getSku());
        Assertions.assertEquals("Logitech Wireless Mouse", actualProduct.getName());
    }

}
