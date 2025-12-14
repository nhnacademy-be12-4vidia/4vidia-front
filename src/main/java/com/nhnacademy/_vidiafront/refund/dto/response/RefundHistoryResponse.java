package com.nhnacademy._vidiafront.refund.dto.response;

import java.time.LocalDate;

public record RefundHistoryResponse(
        Long refundId,
        Long orderId,
        LocalDate orderDate,
        String title,
        int price,
        int quantity,
        LocalDate returnDate,
        String returnStatus,
        String returnReason
){}