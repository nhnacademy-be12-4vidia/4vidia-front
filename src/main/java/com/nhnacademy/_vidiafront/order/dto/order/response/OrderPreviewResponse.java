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


    // 모든 주문아이템 상태가 UNCONFIRMED이면 true
    public boolean hasUnconfirmedItems() {
        if (orderItems == null) return false;
        return orderItems.stream()
                .anyMatch(item -> item.confirmStatus() == null || item.confirmStatus() == ConfirmStatus.UNCONFIRMED);
    }

    public boolean hasUnreturnedItems() {
        if (orderItems == null) return false;
        // 반품 신청 가능한 항목 (확정되지 않았거나, 반품 완료 상태가 아닌 항목)
        return orderItems.stream()
                .anyMatch(item -> item.confirmStatus() != ConfirmStatus.REFUNDED);
    }
}
