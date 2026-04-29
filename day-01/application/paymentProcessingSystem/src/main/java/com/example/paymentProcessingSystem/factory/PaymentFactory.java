package com.example.paymentProcessingSystem.factory;

import com.example.paymentProcessingSystem.service.PaymentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {
    private final Map<String, PaymentService> paymentServiceMap = new HashMap<>();

    public PaymentFactory(List<PaymentService> paymentServices) {
        for (PaymentService service : paymentServices) {
            this.paymentServiceMap.put(service.getPaymentType(), service);
        }
    }

    public PaymentService getPaymentService(String paymentType) {
        PaymentService service = paymentServiceMap.get(paymentType.toUpperCase());
        if (service == null) {
            throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        }
        return service;
    }
}
