package com.example.paymentProcessingSystem.service.impl;

import com.example.paymentProcessingSystem.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaypalPaymentService implements PaymentService {
    @Override
    public String pay(double amount) {
        return "Paid " + amount + " using PayPal.";
    }

    @Override
    public String getPaymentType() {
        return "PAYPAL";
    }

}
