package com.example.paymentProcessingSystem.controller;

import com.example.paymentProcessingSystem.dto.PaymentRequest;
import com.example.paymentProcessingSystem.factory.PaymentFactory;
import com.example.paymentProcessingSystem.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentFactory paymentFactory;

    public PaymentController(PaymentFactory paymentFactory) {
        this.paymentFactory = paymentFactory;
    }

    @PostMapping("/pay")
    public String processPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentService paymentService = paymentFactory.getPaymentService(paymentRequest.getPaymentType());
        return paymentService.pay(paymentRequest.getAmount());
    }

}
