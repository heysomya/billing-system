package com.billingSystem.sale_recording_service.service;

import com.billingSystem.sale_recording_service.entity.Sale;
import com.billingSystem.sale_recording_service.entity.SaleItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ReceiptService {
    private static final BigDecimal TAX_RATE = new BigDecimal("0.07");
    private static final BigDecimal DISCOUNT_THRESHOLD = new BigDecimal("1000.00");
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.05");

    @Autowired
    SalesService salesService;

    public String generateTextReceipt(UUID saleId) {
        Sale sale = salesService.getSaleById(saleId);
        StringBuilder sb = new StringBuilder();
        sb.append("Receipt for Sale: ").append(sale.getId()).append("\n");
        sb.append("Customer: ").append(sale.getCustomer().getFirstName())
                .append(" ").append(sale.getCustomer().getLastName()).append("\n");
        sb.append("Date: ").append(sale.getSaleDate()).append("\n");
        sb.append("Payment Method: ").append(sale.getPaymentMethod()).append("\n");
        sb.append("Items:\n");

        for (SaleItem item : sale.getSaleItem()) {
            sb.append(item.getProduct().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" @ ").append(item.getUnitPrice())
                    .append("\n");
        }

        BigDecimal subtotal = sale.getSaleItem().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = (subtotal.compareTo(DISCOUNT_THRESHOLD) >= 0) ? subtotal.multiply(DISCOUNT_RATE) : BigDecimal.ZERO;
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = taxable.multiply(TAX_RATE);

        sb.append("Subtotal: ").append(subtotal).append("\n");
        sb.append("Discount: -").append(discount).append("\n");
        sb.append("Tax: ").append(tax).append("\n");
        sb.append("Total: ").append(sale.getTotalAmount()).append("\n");

        return sb.toString();
    }

    public byte[] generatePdfReceipt(UUID saleId) throws IOException {
        Sale sale = salesService.getSaleById(saleId);
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream cs = new PDPageContentStream(document, page);

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
        cs.newLineAtOffset(50, 750);
        cs.showText("Sales Receipt");
        cs.endText();

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 12);
        cs.newLineAtOffset(50, 720);
        cs.showText("Sale ID: " + sale.getId());
        cs.newLineAtOffset(0, -15);
        cs.showText("Date: " + sale.getSaleDate());
        cs.newLineAtOffset(0, -15);
        cs.showText("Customer: " + sale.getCustomer().getFirstName() + " " + sale.getCustomer().getLastName());
        cs.newLineAtOffset(0, -15);
        cs.showText("Payment: " + sale.getPaymentMethod());
        cs.endText();

        int y = 650;
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cs.newLineAtOffset(50, y);
        cs.showText("Items:");
        cs.endText();

        y -= 20;
        for (SaleItem item : sale.getSaleItem()) {
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.newLineAtOffset(50, y);
            String line = String.format("%s x%d @ %.2f each", item.getProduct().getName(), item.getQuantity(), item.getUnitPrice());
            cs.showText(line);
            cs.endText();
            y -= 15;
        }

        BigDecimal subtotal = sale.getSaleItem().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discount = (subtotal.compareTo(DISCOUNT_THRESHOLD) >= 0) ? subtotal.multiply(DISCOUNT_RATE) : BigDecimal.ZERO;
        BigDecimal taxable = subtotal.subtract(discount);
        BigDecimal tax = taxable.multiply(TAX_RATE);

        y -= 20;
        cs.beginText();
        cs.newLineAtOffset(50, y);
        cs.showText(String.format("Subtotal: $%.2f", subtotal));
        cs.endText();

        y -= 15;
        cs.beginText();
        cs.newLineAtOffset(50, y);
        cs.showText(String.format("Discount: -$%.2f", discount));
        cs.endText();

        y -= 15;
        cs.beginText();
        cs.newLineAtOffset(50, y);
        cs.showText(String.format("Tax: $%.2f", tax));
        cs.endText();

        y -= 15;
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cs.newLineAtOffset(50, y);
        cs.showText(String.format("Total: $%.2f", sale.getTotalAmount()));
        cs.endText();

        cs.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        return baos.toByteArray();
    }
}

