package com.example.paymentProcessingSystem.service.impl;

import com.example.paymentProcessingSystem.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class UPIPaymentService implements PaymentService {
    @Override
    public String pay(double amount) {
        return "Paid " + amount + " using UPI.";
    }

    @Override
    public String getPaymentType() {
        return "UPI";
    }

}
