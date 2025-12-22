package com.nhnacademy._vidiafront.coupon.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CouponPolicyDto(
        Long policyId,
        String policyName,
        String policyType,
        String discountType,
        Integer discountValue,
        Integer maxDiscountAmount,
        String discountTargetType,
        String validityType,
        Integer validDays,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isActivation,
        String issueStatus
) {}