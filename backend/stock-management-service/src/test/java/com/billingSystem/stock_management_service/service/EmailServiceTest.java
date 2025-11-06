package com.billingSystem.stock_management_service.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailServiceTest {

    @Test
    void testSendStockAlert() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailService service = new EmailService();
        service.mailSender = mailSender;

        String toEmail = "admin@example.com";
        String productName = "Wireless Mouse";
        int quantity = 5;

        service.sendStockAlert(toEmail, productName, quantity);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(mailSender, Mockito.times(1)).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertEquals(toEmail, message.getTo()[0]);
        assertEquals("Low Stock Alert: " + productName, message.getSubject());
        assertTrue(message.getText().contains(String.valueOf(quantity)));
    }
}
