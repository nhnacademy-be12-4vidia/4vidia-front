package com.nhnacademy._vidiafront.order.dto.order.response;

import com.nhnacademy._vidiafront.order.dto.DeliveryStatus;
import com.nhnacademy._vidiafront.order.dto.OrderItemViewStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    ) {
        public boolean isRefundProcessing() { // 반품 진행중
            return orderItemViewStatus == OrderItemViewStatus.REFUND_REQUESTED;
        }

        public boolean isRefunded() { // 반품 완료
            return orderItemViewStatus == OrderItemViewStatus.REFUNDED;
        }

        public boolean isRefundRejected() { // 반품 거절됨
            return orderItemViewStatus == OrderItemViewStatus.REFUND_REJECTED;
        }

        public boolean isConfirmed() { // 구매 확정됨
            return orderItemViewStatus == OrderItemViewStatus.CONFIRMED;
        }

        // 리뷰 작성 가능 여부: 구매확정 상태이고 && 리뷰를 아직 안 썼을 때
        public boolean canWriteReview() {
            return isConfirmed() && (Boolean.FALSE.equals(isReviewed) || isReviewed == null);
        }

        public boolean isReviewCompleted() {
            return isConfirmed() && Boolean.TRUE.equals(isReviewed);
        }
    }

    // --- 주문(Order) 레벨 버튼 노출 로직 ---

    // 1. 주문 취소 가능 (배송 준비중일 때만)
    public boolean canCancel() {
        return deliveryStatus == DeliveryStatus.WAITING;
    }

    // 2. 반품 신청 가능
    // 조건: 배송완료 상태 AND (확정 전 아이템 존재 OR 반품 거절된 아이템 존재)
    public boolean canRequestRefund() {
        if (deliveryStatus != DeliveryStatus.DELIVERED) return false;
        return hasRealItemsToProcess();
    }

    // 3. 전체 구매 확정 가능
    // 조건: 배송완료 상태 AND 반품 진행중인 아이템이 하나도 없어야 함 AND 확정할 아이템이 남아있어야 함
    public boolean canConfirmPurchase() {
        if (deliveryStatus != DeliveryStatus.DELIVERED) return false;

        boolean hasRefundProcessing = orderItems.stream()
                .anyMatch(OrderBookResponse::isRefundProcessing);

        if (hasRefundProcessing) return false; // 반품 진행중인게 있으면 확정 불가

        return hasRealItemsToProcess(); // 확정 또는 반품신청 할 대상이 있어야 함
    }

    // 단순히 상태만 보는 게 아니라, 같은 아이템 ID가 '완료/진행중' 상태를 가지고 있다면
    // 그 아이템의 '거절/미확정' 상태는 무시해야 함
    private boolean hasRealItemsToProcess() {
        if (orderItems == null) return false;

        // 1. 이미 반품 완료되었거나(REFUNDED), 진행중인(REFUND_REQUESTED) 상품들의 ID를 수집
        Set<Long> processedItemIds = orderItems.stream()
                .filter(item -> item.isRefunded() || item.isRefundProcessing())
                .map(OrderBookResponse::orderItemId)
                .collect(Collectors.toSet());

        // 2. 처리해야 할 항목(미확정 or 반품거절)을 찾되, 위에서 수집한 '이미 처리된 ID'는 제외
        return orderItems.stream()
                .filter(item -> !processedItemIds.contains(item.orderItemId())) // 좀비 데이터 무시
                .anyMatch(item -> item.orderItemViewStatus() == OrderItemViewStatus.UNCONFIRMED
                        || item.orderItemViewStatus() == OrderItemViewStatus.REFUND_REJECTED);
    }
}
