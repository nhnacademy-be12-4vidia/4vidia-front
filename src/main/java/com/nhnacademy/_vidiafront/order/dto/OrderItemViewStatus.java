package com.nhnacademy._vidiafront.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderItemViewStatus {
    UNCONFIRMED("구매 확정 전", "text-secondary bg-secondary-light", "fa-hourglass-half"),
    CONFIRMED("구매 확정", "text-success bg-success-light", "fa-check-circle"),
    REFUND_REQUESTED("반품 요청 중", "text-warning bg-warning-light", "fa-undo"),
    REFUNDED("반품 완료", "text-danger bg-danger-light", "fa-ban"),
    REFUND_REJECTED("반품 거절", "text-dark bg-secondary-light", "fa-times-circle");

    private final String description;
    private final String colorClass;
    private final String iconClass;
}