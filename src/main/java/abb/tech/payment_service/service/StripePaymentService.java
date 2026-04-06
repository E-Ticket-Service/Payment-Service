package abb.tech.payment_service.service;

import abb.tech.payment_service.client.StripeClient;
import abb.tech.payment_service.enums.CurrencyEnum;
import abb.tech.payment_service.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;


@Service
@AllArgsConstructor
public class StripePaymentService {

    private final  StripeClient client;


    public Map<String, Object> createPayment(
            BigDecimal amount, String currency, String description, String paymentMethod,Long orderId
            ) {

        return client.createPayment(amount, currency, description,paymentMethod,orderId);
    }
}
