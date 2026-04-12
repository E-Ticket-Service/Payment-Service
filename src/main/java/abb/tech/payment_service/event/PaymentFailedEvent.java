package abb.tech.payment_service.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PaymentFailedEvent {
    Long orderId;
    Long userId;
    String reason;
    LocalDateTime failedAt;
}
