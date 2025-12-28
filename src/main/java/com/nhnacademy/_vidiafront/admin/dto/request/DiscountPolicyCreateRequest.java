package com.nhnacademy._vidiafront.admin.dto.request;

import java.time.LocalDate;

public record DiscountPolicyCreateRequest(
    Long categoryId,
    String discountPolicyName,
    Integer discountRate,
    LocalDate startDate,
    LocalDate endDate
) {}
