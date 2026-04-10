package abb.tech.payment_service.client;

import abb.tech.payment_service.config.StripeFeignConfig;

import abb.tech.payment_service.enums.CurrencyEnum;
import abb.tech.payment_service.enums.PaymentMethod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(
        name = "stripe",
        url = "https://api.stripe.com/v1",
        configuration = StripeFeignConfig.class
)
public interface StripeClient {
    @PostMapping(value = "/payment_intents", consumes = "application/x-www-form-urlencoded")
    Map<String, Object> createPayment(
            @RequestParam(value = "amount") BigDecimal amount,
            @RequestParam(value = "currency") String currency,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "payment_method_types[]") String paymentMethod,
            @RequestParam("metadata[orderId]")Long orderId
    );
}
