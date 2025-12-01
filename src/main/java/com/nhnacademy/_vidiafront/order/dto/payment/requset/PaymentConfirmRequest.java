package com.nhnacademy._vidiafront.order.dto.payment.requset;

public record PaymentConfirmRequest(
        String paymentKey,
        String orderId,
        int amount
) {
}
