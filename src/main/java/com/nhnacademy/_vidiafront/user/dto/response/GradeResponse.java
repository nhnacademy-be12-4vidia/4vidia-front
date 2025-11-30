package com.nhnacademy._vidiafront.user.dto.response;

import lombok.Builder;

@Builder
public record GradeResponse(
        String gradeName,
        Integer pointRate

) {
}
