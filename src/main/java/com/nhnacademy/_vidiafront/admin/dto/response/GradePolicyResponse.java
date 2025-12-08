package com.nhnacademy._vidiafront.admin.dto.response;

public record GradePolicyResponse(
        Long gradeId,
        String gradeName,
        Integer pointRate
) {
}
