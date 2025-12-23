package com.nhnacademy._vidiafront.order.dto.order.response;

import com.nhnacademy._vidiafront.order.dto.DeliveryStatus;
import com.nhnacademy._vidiafront.order.dto.OrderItemViewStatus;

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
            OrderItemViewStatus orderItemViewStatus,
            Boolean isReviewed
    ) { }

    // UNCONFIRMED 상태의 항목이 하나라도 있으면 true (전체 구매 확정 버튼 표시 조건)
    public boolean hasUnconfirmedItems() {
        if (orderItems == null) return false;
        return orderItems.stream()
                .anyMatch(item -> item.orderItemViewStatus() == OrderItemViewStatus.UNCONFIRMED
                        || item.orderItemViewStatus() == OrderItemViewStatus.REFUND_REJECTED);
    }

    // REFUND_REQUEST 상태의 항목 (REFUND_REQUEST 상태가 아닌 항목)이 하나라도 있으면 true
    public boolean hasRefundedRequestItems() {
        if (orderItems == null) return false;
        return orderItems.stream()
                .anyMatch(item -> item.orderItemViewStatus() == OrderItemViewStatus.REFUND_REQUESTED);
    }

    public boolean isRefundAvailable() {
        if (orderItems == null) return false;
        // UNCONFIRMED 또는 REFUND_REJECTED 상태인 아이템이 하나라도 있으면 true
        return orderItems.stream()
                .anyMatch(item -> item.orderItemViewStatus() == OrderItemViewStatus.UNCONFIRMED
                        || item.orderItemViewStatus() == OrderItemViewStatus.REFUND_REJECTED);
    }

    public boolean hasItemsToConfirm() {
        if (orderItems == null) return false;
        return orderItems.stream()
                .anyMatch(item -> item.orderItemViewStatus() == OrderItemViewStatus.UNCONFIRMED
                        || item.orderItemViewStatus() == OrderItemViewStatus.REFUND_REJECTED);
    }
}
