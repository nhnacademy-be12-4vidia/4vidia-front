package com.nhnacademy._vidiafront.point.dto.response;

import java.time.LocalDateTime;

public record PointHistoryResponse(
        LocalDateTime createdAt,
        int price,
        String reason,
        Long orderId,
        LocalDateTime expiredAt
) {}