package com.nhnacademy._vidiafront.point.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PointHistoryResponse(
        LocalDateTime createdAt,
        Integer price,
        String reason,
        String policyName,
        LocalDate expiredDate
) {}