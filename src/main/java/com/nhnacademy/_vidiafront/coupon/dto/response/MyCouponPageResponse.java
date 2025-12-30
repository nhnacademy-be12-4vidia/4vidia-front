package com.nhnacademy._vidiafront.coupon.dto.response;

import com.nhnacademy._vidiafront.global.dto.PageResponse;

public record MyCouponPageResponse(
        PageResponse<MyCouponResponse> page,
        long totalCount,
        long expireSoonCount
) {}
