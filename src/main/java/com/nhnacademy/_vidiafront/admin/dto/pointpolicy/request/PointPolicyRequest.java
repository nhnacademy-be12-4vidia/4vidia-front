package com.nhnacademy._vidiafront.admin.dto.pointpolicy.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PointPolicyRequest(
        @NotBlank
        String pointName,
        @Min(1)
        Integer price
) {}

