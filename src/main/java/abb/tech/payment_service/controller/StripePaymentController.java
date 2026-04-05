package abb.tech.payment_service.controller;

import abb.tech.payment_service.enums.CurrencyEnum;
import abb.tech.payment_service.enums.PaymentMethod;
import abb.tech.payment_service.service.StripePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class StripePaymentController {
    @Autowired
    private final StripePaymentService stripePaymentService;

    public StripePaymentController(StripePaymentService stripePaymentService) {
        this.stripePaymentService = stripePaymentService;
    }

    @GetMapping("/create-intent")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> createStripePayment(
            @RequestParam BigDecimal amount,
            @RequestParam String currency,
            @RequestParam String description,
            @RequestParam String paymentMethod
    ) {

        Map<String, Object> stringObjectMap = stripePaymentService.createPayment(amount, currency, description,paymentMethod);
        return ResponseEntity.ok(stringObjectMap);
    }

}
