package com.example.paymentProcessingSystem.service.impl;

import com.example.paymentProcessingSystem.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class CreditCardPaymentService implements PaymentService {
    @Override
    public String pay(double amount) {
        return "Paid " + amount + " using Credit Card.";
    }

    @Override
    public String getPaymentType() {
        return "CREDIT_CARD";
    }
}
