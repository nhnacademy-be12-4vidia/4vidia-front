package com.nhnacademy._vidiafront.user.dto.grade.response;

import lombok.Builder;

@Builder
public record GradePolicyResponse(
        String gradeName,
        Integer pointRate,
        Long minNetAmount,
        Long maxNetAmount,
        String desc
) {}
