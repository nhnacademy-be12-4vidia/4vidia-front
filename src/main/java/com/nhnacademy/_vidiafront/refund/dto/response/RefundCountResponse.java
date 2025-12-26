package com.nhnacademy._vidiafront.refund.dto.response;

public record RefundCountResponse(
        long total,
        long process,
        long approved
) {}
