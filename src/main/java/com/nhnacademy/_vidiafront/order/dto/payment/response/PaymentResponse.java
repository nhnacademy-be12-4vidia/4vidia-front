package com.nhnacademy._vidiafront.order.dto.payment.response;

public record PaymentResponse(
    Long orderId,
    String payStatus,
    Integer amount
) { }
