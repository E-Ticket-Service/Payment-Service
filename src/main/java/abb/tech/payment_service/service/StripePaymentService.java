package abb.tech.payment_service.service;

import abb.tech.payment_service.client.StripeClient;
import abb.tech.payment_service.event.PaymentFailedEvent;
import abb.tech.payment_service.event.PaymentSuccessEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static abb.tech.payment_service.constant.KafkaConstant.PAYMENT_FAILED_TOPIC;
import static abb.tech.payment_service.constant.KafkaConstant.PAYMENT_SUCCESS_TOPIC;


@Service
@AllArgsConstructor
public class StripePaymentService {

    private final StripeClient client;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public Map<String, Object> createPayment(
            BigDecimal amount, String currency, String description, String paymentMethod
            , Long orderId, Long userId, String userEmail
    ) {
        Long amountInCents = amount
                .multiply(BigDecimal.valueOf(100))
                .longValueExact();

        try {
            Map<String, Object> result = client.createPayment(amountInCents, currency, description, paymentMethod, orderId);

            String transactionId = (String) result.get("id");
            PaymentSuccessEvent successEvent = PaymentSuccessEvent.builder()
                    .paymentId(transactionId)
                    .orderId(orderId)
                    .userId(userId)
                    .amount(amount)
                    .paidAt(LocalDateTime.now())
                    .status("SUCCESS")
                    .userEmail(userEmail)
                    .build();
            kafkaTemplate.send(PAYMENT_SUCCESS_TOPIC, successEvent);

            return result;
        } catch (Exception e) {
            PaymentFailedEvent failedEvent = PaymentFailedEvent.builder()
                    .orderId(orderId)
                    .userId(userId)
                    .reason(e.getMessage())
                    .failedAt(LocalDateTime.now())
                    .build();
            kafkaTemplate.send(PAYMENT_FAILED_TOPIC, failedEvent);
            throw e;
        }
    }

    public Map<String, Object> refund(String paymentIntentId, BigDecimal amount) {
        Long amountInCents = amount != null ? amount.multiply(BigDecimal.valueOf(100)).longValueExact() : null;
        return client.refundPayment(paymentIntentId, amountInCents);
    }
}
