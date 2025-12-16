package com.nhnacademy._vidiafront.coupon.dto;

import java.time.LocalDateTime;

public record MyCouponResponse(
        Long couponId,
        String policyName,
        String discountType,
        Integer discountValue,
        Integer maxDiscountAmount,
        String targetType,
        String categoryKdcId,
        Long bookId,
        LocalDateTime issuedAt,
        LocalDateTime expireAt,
        String status,
        Long usedOrderId
) {}
