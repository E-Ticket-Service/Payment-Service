package abb.tech.payment_service.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentSuccessEvent {
    String paymentId;
    Long orderId;
    Long userId;
    BigDecimal amount;
    LocalDateTime paidAt;
    String status;
    String userEmail;
}
