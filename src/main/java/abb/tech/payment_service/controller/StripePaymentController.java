package abb.tech.payment_service.controller;


import abb.tech.payment_service.service.StripePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class StripePaymentController {
    @Autowired
    private final StripePaymentService stripePaymentService;

    public StripePaymentController(StripePaymentService stripePaymentService) {
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/create-intent")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> createStripePayment(
            @RequestParam BigDecimal amount,
            @RequestParam String currency,
            @RequestParam String description,
            @RequestParam String paymentMethod,
            @RequestParam Long orderId
    ) {
        Map<String, Object> stringObjectMap = stripePaymentService.createPayment(amount, currency, description, paymentMethod, orderId);
        return ResponseEntity.ok(stringObjectMap);
    }

    @PostMapping("/refund")
    public ResponseEntity<Map<String, Object>> refundPayment(
            @RequestParam String paymentIntentId,
            @RequestParam(required = false) BigDecimal amount
    ) {
        Map<String, Object> refund = stripePaymentService.refund(paymentIntentId, amount);
        return ResponseEntity.ok(refund);
    }

}
