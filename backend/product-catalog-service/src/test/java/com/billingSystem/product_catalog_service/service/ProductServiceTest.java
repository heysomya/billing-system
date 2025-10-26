package com.billingSystem.product_catalog_service.service;

import com.billingSystem.product_catalog_service.entity.Product;
import com.billingSystem.product_catalog_service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product updatedProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product1 = new Product(1L, "Mouse", 10.0, "Wireless Mouse", "Electronics", "SKU1", 100);
        updatedProduct = new Product(null, "Mouse Pro", 15.0, "Upgraded Mouse", "Electronics", "SKU1-PRO", 80);
    }

    @Test
    void testCreateProduct() {
        Mockito.when(productRepository.save(product1)).thenReturn(product1);

        Product created = productService.create(product1);

        Assertions.assertEquals("Mouse", created.getName());
        Assertions.assertEquals(10.0, created.getPrice());
    }

    @Test
    void testFindAllProducts() {
        List<Product> products = Arrays.asList(product1);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Mouse", result.get(0).getName());
    }

    @Test
    void testFindByIdFound() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Optional<Product> result = productService.findById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Mouse", result.get().getName());
    }

    @Test
    void testFindByIdNotFound() {
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.findById(2L);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testUpdateProductSuccess() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        Product result = productService.update(1L, updatedProduct);

        Assertions.assertEquals("Mouse Pro", result.getName());
        Assertions.assertEquals(15.0, result.getPrice());
    }

    @Test
    void testUpdateProductNotFound() {
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            productService.update(2L, updatedProduct);
        });

        Assertions.assertTrue(exception.getMessage().contains("Product not found"));
    }

    @Test
    void testDeleteProduct() {
        Mockito.doNothing().when(productRepository).deleteById(1L);

        // No exception means success
        productService.delete(1L);

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testSearchByName() {
        List<Product> products = Arrays.asList(product1);
        Mockito.when(productRepository.findByName("Mouse")).thenReturn(products);

        List<Product> result = productService.searchByName("Mouse");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Mouse", result.get(0).getName());
    }

    @Test
    void testSearchByCategory() {
        List<Product> products = Arrays.asList(product1);
        Mockito.when(productRepository.findByCategory("Electronics")).thenReturn(products);

        List<Product> result = productService.searchByCategory("Electronics");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Mouse", result.get(0).getName());
    }

    @Test
    void testSearchBySKU() {
        Mockito.when(productRepository.findBySku("SKU1")).thenReturn(product1);

        Product result = productService.searchBySKU("SKU1");

        Assertions.assertEquals("SKU1", result.getSku());
        Assertions.assertEquals("Mouse", result.getName());
    }
}
