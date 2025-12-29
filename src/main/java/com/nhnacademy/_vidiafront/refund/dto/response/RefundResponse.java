package com.nhnacademy._vidiafront.refund.dto.response;

import java.util.List;
/**
 * 반품 신청 페이지 (반품 가능 아이템 띄우기)
 */
public record RefundResponse (
        Long orderId,
        boolean canReturnByChangeOfMind, // 단순 변심 반품 가능 여부
        List<OrderItemResponse> orderItems
){
}
