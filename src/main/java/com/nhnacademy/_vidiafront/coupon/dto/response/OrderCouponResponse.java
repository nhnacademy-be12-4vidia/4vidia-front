package com.nhnacademy._vidiafront.coupon.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCouponResponse(
        List<OrderPageCouponResponse> possibleCoupons,
        List<OrderPageCouponResponse> impossibleCoupons
) {
    public record OrderPageCouponResponse(
            Long couponId,
            String policyName,
            Integer maxDiscountAmount, // 최대할인금액
            String discountType,       // PRICE / RATE 구분
            Integer discountValue,     // 5000(₩) or 10(%)
            Integer discountPrice,     // 적용시 할인금액: 5000(₩) or 만원 보냈을 때 10% 적용 1000(₩)
            LocalDateTime expireAt,
            boolean available,
            String reason
    ) {}
}
