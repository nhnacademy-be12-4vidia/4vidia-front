package com.nhnacademy._vidiafront.admin.dto.response;

import java.util.List;

public record CouponPolicyResponse(
        Long policyId,
        String policyName,
        String policyType,
        String discountType,
        String discountTargetType,
        Integer discountValue,
        String categoryKdcId,
        Long bookId,
        String validityType,
        Integer validDays,

        List<Integer> startDate,
        List<Integer> endDate,

        Integer limitedQuantity,
        Integer issuedQuantity,
        Integer minOrderAmount,
        Integer maxDiscountAmount,
        Boolean isActivation
) {}
