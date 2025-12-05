package com.nhnacademy._vidiafront.order.dto.order.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCheckoutResponse(
        List<OrderBookResponse> bookItems,
        String orderName,
        int finalAmount,

        String name,
        String email,
        String phone,
        Integer point,
        List<AddressResponse> addressResponses,

        List<DeliveryDateResponse> deliveryDateResponses,
        List<OrderPageCouponResponse> possibleCoupons,
        List<OrderPageCouponResponse> impossibleCoupons,
        List<PackagingOptionResponse> packagingOptions
) {
    public record OrderBookResponse(
            Long bookId,
            String bookTitle,
            String bookAuthor,
            String bookImageUrl,
            Integer quantity,
            Integer salePrice
    ) { }

    public record AddressResponse(
            Long addressId,
            String alias,
            String roadAddress,
            String zipCode,
            String addressDetail
    ) { }

    public record DeliveryDateResponse(
            String value,
            String displayDate
    ) { }

    public record OrderPageCouponResponse(
            Long couponId,
            String policyName,
            String discountType,       // PRICE / RATE 구분
            Integer discountValue,     // 5000(₩) or 10(%)
            Integer discountPrice,     // 적용시 할인금액: 5000(₩) or 만원 보냈을 때 10% 적용 1000(₩)
            LocalDateTime expireAt,
            boolean available,
            String reason
    ) { }

    public record PackagingOptionResponse(
            long packagingOptionId,
            String name,
            int price
    ) { }
}