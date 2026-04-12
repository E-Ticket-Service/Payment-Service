package abb.tech.payment_service.listener;

import abb.tech.payment_service.event.OrderCreatedEvent;
import abb.tech.payment_service.event.PaymentFailedEvent;
import abb.tech.payment_service.event.PaymentSuccessEvent;
import abb.tech.payment_service.service.StripePaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static abb.tech.payment_service.constant.KafkaConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentKafkaListener {

    private final StripePaymentService stripePaymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = ORDER_CREATED_TOPIC, groupId = PAYMENT_SERVICE_GROUP)
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            log.info("Payment started for Order ID: {}", event.getOrderId());

            Map<String, Object> result = stripePaymentService.createPayment(
                    event.getTotalAmount(),
                    event.getCurrency(),
                    event.getDescription(),
                    event.getPaymentMethod(),
                    event.getOrderId()
            );

            String transactionId = (String) result.get("id");
            PaymentSuccessEvent successEvent = PaymentSuccessEvent.builder()
                    .paymentId(transactionId)
                    .orderId(event.getOrderId())
                    .userId(event.getUserId())
                    .amount(event.getTotalAmount())
                    .paidAt(LocalDateTime.now())
                    .status("SUCCESS")
                    .userEmail(event.getUserEmail())
                    .build();
            kafkaTemplate.send(PAYMENT_SUCCESS_TOPIC, successEvent);
            log.info("Payment success event sent for Order ID: {}", event.getOrderId());

        } catch (Exception e) {
            log.error("Payment error for Order ID: {}", event.getOrderId(), e);

            PaymentFailedEvent failedEvent = PaymentFailedEvent.builder()
                    .orderId(event.getOrderId())
                    .userId(event.getUserId())
                    .reason(e.getMessage())
                    .failedAt(LocalDateTime.now())
                    .build();
            kafkaTemplate.send(PAYMENT_FAILED_TOPIC, failedEvent);
            log.info("Payment failed event sent for Order ID: {}", event.getOrderId());
        }
    }
}
