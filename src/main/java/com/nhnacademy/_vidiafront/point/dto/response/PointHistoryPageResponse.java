package com.nhnacademy._vidiafront.point.dto.response;

import java.util.List;

public record PointHistoryPageResponse(
        List<PointHistoryResponse> content,
        int totalPages,
        int totalElements,
        int size,
        int number,
        boolean first,
        boolean last
) {}