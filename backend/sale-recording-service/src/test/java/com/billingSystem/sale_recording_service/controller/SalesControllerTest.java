package com.billingSystem.sale_recording_service.controller;

import com.billingSystem.sale_recording_service.entity.Sale;
import com.billingSystem.sale_recording_service.entity.SaleRequest;
import com.billingSystem.sale_recording_service.service.ReceiptService;
import com.billingSystem.sale_recording_service.service.SalesService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SalesControllerTest {

    @Test
    public void testRecordSale() {
        SalesService salesService = Mockito.mock(SalesService.class);
        ReceiptService receiptService = Mockito.mock(ReceiptService.class);
        SalesController controller = new SalesController();
        controller.salesService = salesService;
        controller.receiptService = receiptService;

        SaleRequest request = new SaleRequest();
        Sale mockSale = new Sale();

        Mockito.when(salesService.recordSale(request)).thenReturn(mockSale);

        ResponseEntity<Sale> response = controller.recordSale(request);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(mockSale, response.getBody());
    }

    @Test
    public void testGetReceiptText() {
        ReceiptService receiptService = Mockito.mock(ReceiptService.class);
        SalesController controller = new SalesController();
        controller.receiptService = receiptService;

        UUID id = UUID.randomUUID();
        String expectedReceipt = "Test Receipt";

        Mockito.when(receiptService.generateTextReceipt(id)).thenReturn(expectedReceipt);

        ResponseEntity<String> response = controller.getReceiptText(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedReceipt, response.getBody());
    }

    @Test
    public void testGetReceiptPdf() throws IOException {
        ReceiptService receiptService = Mockito.mock(ReceiptService.class);
        SalesController controller = new SalesController();
        controller.receiptService = receiptService;

        UUID id = UUID.randomUUID();
        byte[] pdfBytes = new byte[] {1, 2, 3};

        Mockito.when(receiptService.generatePdfReceipt(id)).thenReturn(pdfBytes);

        ResponseEntity<byte[]> response = controller.getReceiptPdf(id);

        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(pdfBytes, response.getBody());
        assertEquals("application/pdf", response.getHeaders().getContentType().toString());
    }
}
