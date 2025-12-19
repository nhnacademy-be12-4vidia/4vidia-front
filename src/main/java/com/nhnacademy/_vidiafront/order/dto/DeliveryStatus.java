package com.nhnacademy._vidiafront.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {
    WAITING(0, "배송 준비중", "text-warning bg-warning-light", "fa-box-open"),
    SHIPPING(1, "배송 중", "text-info bg-info-light", "fa-truck"),
    DELIVERED(2, "배송 완료", "text-success bg-success-light", "fa-check-circle"),
    CANCELED(3, "주문 취소", "text-secondary bg-secondary-light", "fa-times-circle");

    private final int code;
    private final String description;
    private final String colorClass;
    private final String iconClass;

    public static DeliveryStatus fromCode(int code) {
        for (DeliveryStatus status : DeliveryStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
