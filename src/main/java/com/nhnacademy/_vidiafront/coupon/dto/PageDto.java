package com.nhnacademy._vidiafront.coupon.dto;

import java.util.List;

public record PageDto<T>(
        List<T> content,
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {}
