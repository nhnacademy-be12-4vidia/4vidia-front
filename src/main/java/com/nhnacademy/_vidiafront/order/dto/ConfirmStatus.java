package com.nhnacademy._vidiafront.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfirmStatus {
    UNCONFIRMED(0, "구매 확정 전", "text-secondary bg-secondary-light", "fa-hourglass-half"),
    CONFIRMED(1, "구매 확정", "text-success bg-success-light", "fa-check-circle");

    private final int code;
    private final String description;
    private final String colorClass;
    private final String iconClass;

    public static ConfirmStatus fromCode(int code) {
        for (ConfirmStatus status : ConfirmStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
