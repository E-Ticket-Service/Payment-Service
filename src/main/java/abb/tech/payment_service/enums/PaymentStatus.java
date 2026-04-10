package abb.tech.payment_service.enums;

public enum PaymentStatus {
    PENDING("Gözləmədə"), SUCCESSFULLY("Uğurlu"), FAILED("Uğursuz");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
