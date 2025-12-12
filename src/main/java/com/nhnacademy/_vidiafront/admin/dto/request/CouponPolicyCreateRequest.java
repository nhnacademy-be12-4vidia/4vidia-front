package com.nhnacademy._vidiafront.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

@Builder
public record CouponPolicyCreateRequest(
        String policyName,
        String policyType,
        String discountType,
        Integer discountValue,
        String discountTargetType,
        String categoryKdcId,
        Long bookId,
        String validityType,
        Integer validDays,
        String startDate,
        String endDate,
        Integer limitedQuantity,
        Integer minOrderAmount,
        Integer maxDiscountAmount,
        Boolean isActivation
) {}
