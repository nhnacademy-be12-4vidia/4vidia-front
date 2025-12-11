package com.nhnacademy._vidiafront.order.dto.order.response;

import com.nhnacademy._vidiafront.order.dto.ConfirmStatus;
import com.nhnacademy._vidiafront.order.dto.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderPreviewResponse(
        long orderId,
        long userId,
        LocalDateTime createdAt,
        DeliveryStatus deliveryStatus,
        List<OrderBookResponse> orderItems
) {
    public record OrderBookResponse(
            Long orderItemId,
            Long bookId,
            String bookTitle,
            String bookAuthor,
            String bookImageUrl,
            Integer quantity,
            Integer salePrice,
            ConfirmStatus confirmStatus,
            Boolean isReviewed
    ) { }

    // UNCONFIRMED 상태의 항목이 하나라도 있으면 true (전체 구매 확정 버튼 표시 조건)
    public boolean hasUnconfirmedItems() {
        if (orderItems == null) return false;
        return orderItems.stream()
                .anyMatch(item -> item.confirmStatus() == ConfirmStatus.UNCONFIRMED);
    }

    // 반품 신청 가능한 항목 (REFUNDED 또는 REFUND_REQUEST 상태가 아닌 항목)이 하나라도 있으면 true
//    public boolean hasUnreturnedItems() {
//        if (orderItems == null) return false;
//        // 반품 완료 (REFUNDED) 또는 반품 요청 중 (REFUND_REQUEST) 상태가 아닌 항목이 하나라도 있으면 반품 신청 가능
//        return orderItems.stream()
//                .anyMatch(item -> item.confirmStatus() != ConfirmStatus.REFUNDED
//                        && item.confirmStatus() != ConfirmStatus.REFUND_REQUEST);
//    }
}
