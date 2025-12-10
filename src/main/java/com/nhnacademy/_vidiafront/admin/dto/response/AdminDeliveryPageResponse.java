package com.nhnacademy._vidiafront.admin.dto.response;

import java.util.List;

public record AdminDeliveryPageResponse(
        List<DeliveryResponse> content,
        int number,           // 현재 페이지 (0-based)
        int size,             // 페이지 크기
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {}
