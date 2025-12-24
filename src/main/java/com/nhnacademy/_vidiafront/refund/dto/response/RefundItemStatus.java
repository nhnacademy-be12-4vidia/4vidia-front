package com.nhnacademy._vidiafront.refund.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RefundItemStatus {
    PROCESS(0, "반품 신청", "text-info"), // 반품 신청 ~ 결과 나오기 전까지
    APPROVED(1, "반품 승인", "text-success"), // 승인
    REJECTED(2, "반품 거절", "text-danger"); // 거절

    private final int code;
    private final String description;
    private final String colorClass;
}
