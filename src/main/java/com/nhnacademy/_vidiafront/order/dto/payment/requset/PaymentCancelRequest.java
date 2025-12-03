package com.nhnacademy._vidiafront.order.dto.payment.requset;

public record PaymentCancelRequest(
        String paymentKey, //결제 내역에 존재
        String reason,
        long amount
) {
}
