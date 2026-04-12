package abb.tech.payment_service.listener;

import abb.tech.payment_service.event.OrderCreatedEvent;
import abb.tech.payment_service.event.PaymentFailedEvent;
import abb.tech.payment_service.event.PaymentSuccessEvent;
import abb.tech.payment_service.event.RefundRequestEvent;
import abb.tech.payment_service.event.RefundResultEvent;
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


    @KafkaListener(topics = REFUND_REQUEST_TOPIC, groupId = PAYMENT_SERVICE_GROUP)
    public void handleRefundRequest(RefundRequestEvent event) {
        try {
            log.info("Refund started for Payment Intent ID: {}, Order ID: {}", event.getPaymentIntentId(), event.getOrderId());

            Map<String, Object> result = stripePaymentService.refund(event.getPaymentIntentId(), event.getAmount());

            RefundResultEvent resultEvent = RefundResultEvent.builder()
                    .refundId((String) result.get("id"))
                    .paymentIntentId(event.getPaymentIntentId())
                    .orderId(event.getOrderId())
                    .amount(event.getAmount())
                    .status("SUCCESS")
                    .timestamp(LocalDateTime.now())
                    .build();

            kafkaTemplate.send(REFUND_RESULT_TOPIC, resultEvent);
            log.info("Refund success event sent for Order ID: {}", event.getOrderId());

        } catch (Exception e) {
            log.error("Refund error for Order ID: {}", event.getOrderId(), e);

            RefundResultEvent resultEvent = RefundResultEvent.builder()
                    .paymentIntentId(event.getPaymentIntentId())
                    .orderId(event.getOrderId())
                    .amount(event.getAmount())
                    .status("FAILED")
                    .reason(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

            kafkaTemplate.send(REFUND_RESULT_TOPIC, resultEvent);
            log.info("Refund failed event sent for Order ID: {}", event.getOrderId());
        }
    }
}
