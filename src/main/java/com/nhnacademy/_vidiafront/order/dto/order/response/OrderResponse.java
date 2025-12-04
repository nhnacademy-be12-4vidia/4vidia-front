package com.nhnacademy._vidiafront.order.dto.order.response;

import com.nhnacademy._vidiafront.order.dto.ConfirmStatus;
import com.nhnacademy._vidiafront.order.dto.DeliveryStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        long orderId,
        long userId,
        String recipientName,
        String addressRoadname,
        String addressDetail,
        String zipCode,
        String recipientPhone,
        String deliveryRequest,
        LocalDateTime createdAt,
        int couponDiscount,
        int pointUsed,
        LocalDate deliveryDate,
        DeliveryStatus deliveryStatus,
        LocalDate actualDeliveryDate, //null값 가져올수도있음
        int totalPrice, //도서 + 포장 + 배송
        int payPrice,   //도서 + 포장 + 배송 - 포인트 - 쿠폰
        List<OrderBookResponse> orderItems,
        int itemsPrice  //순수 도서 금액 (도서 * 수량)의 합
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
            List<PackagingResponse> packagingResponses,
            int totalPackagingPrice
    ) {
        public record PackagingResponse(
                Long packagingOptionId,
                String name,
                int price
        ) { }
    }
}
