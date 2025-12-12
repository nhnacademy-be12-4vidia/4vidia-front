package com.nhnacademy._vidiafront.coupon.dto;

import java.time.LocalDateTime;

public record CouponPolicyDto(
        Long policyId,
        String policyName,
        String discountType,
        Integer discountValue,
        Integer maxDiscountAmount,
        String discountTargetType,
        String validityType,
        Integer validDays,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean isActivation
) {}