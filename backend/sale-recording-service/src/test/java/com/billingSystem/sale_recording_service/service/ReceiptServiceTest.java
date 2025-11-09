package com.billingSystem.sale_recording_service.service;

import com.billingSystem.sale_recording_service.entity.Customers;
import com.billingSystem.sale_recording_service.entity.Product;
import com.billingSystem.sale_recording_service.entity.Sale;
import com.billingSystem.sale_recording_service.entity.SaleItem;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptServiceTest {

    @Test
    public void testGenerateTextReceipt() {
        SalesService salesService = Mockito.mock(SalesService.class);
        ReceiptService receiptService = new ReceiptService();
        receiptService.salesService = salesService;

        UUID saleId = UUID.randomUUID();

        Sale sale = new Sale();
        sale.setId(saleId);
        Customers customer = new Customers();
        customer.setFirstName("customer");
        customer.setLastName("A");
        sale.setCustomer(customer);
        sale.setPaymentMethod("CREDIT_CARD");

        SaleItem item = new SaleItem();
        Product product = new Product();
        product.setName("MOUSE");
        item.setProduct(product);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("100"));

        sale.setSaleItem(List.of(item));
        sale.setTotalAmount(new BigDecimal("110"));

        Mockito.when(salesService.getSaleById(saleId)).thenReturn(sale);

        String receipt = receiptService.generateTextReceipt(saleId);

        assertTrue(receipt.contains("customer A"));
        assertTrue(receipt.contains("MOUSE"));
        assertTrue(receipt.contains("Total: 110"));
    }

    @Test
    public void testGeneratePdfReceipt_NotThrows() throws Exception {
        SalesService salesService = Mockito.mock(SalesService.class);
        ReceiptService receiptService = new ReceiptService();
        receiptService.salesService = salesService;

        UUID saleId = UUID.randomUUID();

        Sale sale = new Sale();
        sale.setId(saleId);
        Customers customer = new Customers();
        customer.setFirstName("Customer");
        customer.setLastName("B");
        sale.setCustomer(customer);
        sale.setPaymentMethod("Cash");

        SaleItem item = new SaleItem();
        Product product = new Product();
        product.setName("keyboard");
        item.setProduct(product);
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("50"));

        sale.setSaleItem(List.of(item));
        sale.setTotalAmount(new BigDecimal("110"));

        Mockito.when(salesService.getSaleById(saleId)).thenReturn(sale);

        byte[] pdfBytes = receiptService.generatePdfReceipt(saleId);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }
}

