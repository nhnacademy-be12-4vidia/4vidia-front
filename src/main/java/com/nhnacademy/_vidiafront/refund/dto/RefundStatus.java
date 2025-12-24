package com.nhnacademy._vidiafront.refund.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RefundStatus {
    PROCESS(0, "반품 진행 중", "text-warning" ), // 반품 신청 ~ 결과 나오기 전까지
    APPROVED(1, "반품 처리 완료", "text-success" ); // 처리 완료

    private final int code;
    private final String description;
    private final String colorClass;
}
