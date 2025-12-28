package com.nhnacademy._vidiafront.admin.dto.request;

import java.time.LocalDate;

public record DiscountPolicyUpdateRequest(
    String discountPolicyName,
    Integer discountRate,
    LocalDate startDate,
    LocalDate endDate
) {}
