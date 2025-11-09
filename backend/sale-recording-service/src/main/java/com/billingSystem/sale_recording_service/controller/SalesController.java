package com.billingSystem.sale_recording_service.controller;

import com.billingSystem.sale_recording_service.entity.Sale;
import com.billingSystem.sale_recording_service.entity.SaleRequest;
import com.billingSystem.sale_recording_service.service.ReceiptService;
import com.billingSystem.sale_recording_service.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    SalesService salesService;

    @Autowired
    ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<Sale> recordSale(@RequestBody SaleRequest request) {
        Sale sale = salesService.recordSale(request);
        return ResponseEntity.ok(sale);
    }

    @GetMapping("/{id}/receipt/text")
    public ResponseEntity<String> getReceiptText(@PathVariable UUID id) {
        String receipt = receiptService.generateTextReceipt(id);
        return ResponseEntity.ok(receipt);
    }

    // Get PDF receipt for a sale
    @GetMapping("/{id}/receipt/pdf")
    public ResponseEntity<byte[]> getReceiptPdf(@PathVariable UUID id) throws IOException {
        byte[] pdf = receiptService.generatePdfReceipt(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("receipt-" + id + ".pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
