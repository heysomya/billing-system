package com.billingSystem.stock_management_service.controller;

import com.billingSystem.stock_management_service.entity.InventoryLog;
import com.billingSystem.stock_management_service.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class StockControllerTest {

    @Test
    void testUpdateStock() {
        StockService stockService = Mockito.mock(StockService.class);
        StockController controller = new StockController(stockService);

        UUID productId = UUID.randomUUID();
        Mockito.when(stockService.updateStock(productId, 15, "restock")).thenReturn("Stock updated");

        ResponseEntity<String> response = controller.updateStock(productId, 15, "restock");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Stock updated", response.getBody());
    }

    @Test
    void testGetLogsForProduct() {
        StockService stockService = Mockito.mock(StockService.class);
        StockController controller = new StockController(stockService);

        UUID productId = UUID.randomUUID();
        InventoryLog log = new InventoryLog();
        List<InventoryLog> logs = Arrays.asList(log);

        Mockito.when(stockService.getInventoryLogs(productId)).thenReturn(logs);

        List<InventoryLog> result = controller.getLogsForProduct(productId);

        assertEquals(1, result.size());
        assertSame(log, result.get(0));
    }

    @Test
    void testGetStock() {
        StockService stockService = Mockito.mock(StockService.class);
        StockController controller = new StockController(stockService);

        UUID productId = UUID.randomUUID();
        Mockito.when(stockService.getQuantity(productId)).thenReturn(25);

        Integer stock = controller.getStock(productId);

        assertEquals(25, stock);
    }
}
