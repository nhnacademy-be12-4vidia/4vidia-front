package com.nhnacademy._vidiafront.admin.dto.response;

import java.time.LocalDate;

public record DiscountPolicyResponse(
    Long id,
    Long categoryId,
    String categoryName,
    String kdcCode,
    String discountPolicyName,
    Integer discountRate,
    LocalDate startDate,
    LocalDate endDate
) {}
