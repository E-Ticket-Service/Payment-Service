package abb.tech.payment_service.model;

import abb.tech.payment_service.enums.CurrencyEnum;
import abb.tech.payment_service.enums.PaymentMethod;
import abb.tech.payment_service.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    @Column(name = "user_id")
    Long userId;
    @Column(name = "ticket_id")
    Long ticketId;
    @Column(precision = 10, scale = 2, nullable = false)
    BigDecimal amount;
    @Enumerated(EnumType.STRING)
    CurrencyEnum currency;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    PaymentStatus paymentStatus;
    @Column(name = "transaction_id", unique = true, nullable = false)
    String transactionId;
    @Column(name = "created_at", nullable = false)
    LocalDateTime createAt;
    @Column(name = "confirmed_at", nullable = false)
    LocalDateTime confirmedAt;


}
