package com.billingSystem.stock_management_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    public void sendStockAlert(String toEmail, String productName, int quantity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Low Stock Alert: " + productName);
        message.setText("Stock for product '" + productName + "' is low: " + quantity + " units left.");
        mailSender.send(message);
    }
}

