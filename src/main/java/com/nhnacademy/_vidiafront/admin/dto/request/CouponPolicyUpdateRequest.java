package com.nhnacademy._vidiafront.admin.dto.request;

import lombok.Builder;

@Builder
public record CouponPolicyUpdateRequest(
        String policyName,
        Integer discountValue,
        String discountTargetType,
        Integer minOrderAmount,
        Integer maxDiscountAmount
) {}
