package com.nhnacademy._vidiafront.point.dto.response;

import java.time.LocalDateTime;

public record PointHistoryResponse(
        LocalDateTime createdAt,
        Integer price,
        String reason,
        String policyName,
        LocalDateTime expiredAt
) {}