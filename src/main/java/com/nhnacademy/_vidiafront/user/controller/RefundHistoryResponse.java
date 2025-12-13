package com.nhnacademy._vidiafront.user.controller;

import java.time.LocalDate;

// todo : 반품조회에서 사용할 임시 dto 입니다. 삭제 및 수정해주세요
// 뷰에서 item.title() 형태로 호출하므로 Record 사용이 적합합니다.
public record RefundHistoryResponse(
    Long id, // refundId
    Long bookId,
    String orderId,
    LocalDate orderDate,
    String title,
    String author,
    int price,
    int quantity,
    String imageUrl,      // HTML의 'image' 필드
    LocalDate returnDate,
    RefundStatus returnStatus, // Enum 사용
    String returnReason,
    String returnType,
    int refundAmount,
    int returnFee,
    String refundAccount,
    String rejectReason,
    String trackingNumber
) {}

// 상태값 매핑을 위한 Enum
enum RefundStatus {
    REQUESTED("반품신청"),
    COMPLETED("반품완료"),
    REJECTED("반품거부");

    private final String label;
    RefundStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}