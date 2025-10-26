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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product1 = new Product(1L, "Mouse", 10.0, "Wireless Mouse", "Electronics", "SKU1", 100);
        product2 = new Product(2L, "Keyboard", 20.0, "Mechanical Keyboard", "Electronics", "SKU2", 150);
    }

    @Test
    void testGetProductByIdDirect() {
        Mockito.when(productService.findById(1L)).thenReturn(Optional.of(product1));

        ResponseEntity<Product> response = productController.getProductById(1L);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("Mouse", response.getBody().getName());
    }

    @Test
    void testGetProductByIdNotFound() {
        Mockito.when(productService.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(2L);

        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertNull(response.getBody());
    }

    @Test
    void testCreateProduct() {
        Mockito.when(productService.create(product1)).thenReturn(product1);

        ResponseEntity<Product> response = productController.createProduct(product1);

        Assertions.assertEquals(201, response.getStatusCodeValue());
        Assertions.assertEquals("Mouse", response.getBody().getName());
    }

    @Test
    void testUpdateProduct() {
        Mockito.when(productService.update(1L, product1)).thenReturn(product1);

        ResponseEntity<Product> response = productController.updateProduct(1L, product1);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("Mouse", response.getBody().getName());
    }

    @Test
    void testUpdateProductNotFound() {
        Mockito.when(productService.update(2L, product1)).thenThrow(new RuntimeException("Product not found"));

        ResponseEntity<Product> response = productController.updateProduct(2L, product1);

        Assertions.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteProduct() {
        Mockito.doNothing().when(productService).delete(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        Assertions.assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product1, product2);

        Mockito.when(productService.findAll()).thenReturn(products);

        List<Product> actualProducts = productController.getAllProducts();

        Assertions.assertEquals(2, actualProducts.size());
        Assertions.assertEquals("Mouse", actualProducts.get(0).getName());
        Assertions.assertEquals("Keyboard", actualProducts.get(1).getName());
    }

    @Test
    void testSearchProductsByName() {
        List<Product> searchResult = Arrays.asList(product1);

        Mockito.when(productService.searchByName("Mouse")).thenReturn(searchResult);

        List<Product> actualProducts = productController.searchProductsByName("Mouse");

        Assertions.assertEquals(1, actualProducts.size());
        Assertions.assertEquals("Mouse", actualProducts.get(0).getName());
    }

    @Test
    void testSearchProductsByCategory() {
        List<Product> searchResult = Arrays.asList(product1, product2);

        Mockito.when(productService.searchByCategory("Electronics")).thenReturn(searchResult);

        List<Product> actualProducts = productController.searchProductsByCategory("Electronics");

        Assertions.assertEquals(2, actualProducts.size());
        Assertions.assertEquals("Mouse", actualProducts.get(0).getName());
        Assertions.assertEquals("Keyboard", actualProducts.get(1).getName());
    }

    @Test
    void testSearchProductsBySKU() {
        Mockito.when(productService.searchBySKU("SKU1")).thenReturn(product1);

        Product actualProduct = productController.searchProductsBySKU("SKU1");

        Assertions.assertNotNull(actualProduct);
        Assertions.assertEquals("SKU1", actualProduct.getSku());
        Assertions.assertEquals("Mouse", actualProduct.getName());
    }

}
