package com.nhnacademy._vidiafront.order.dto.order.response;

public record OrderCountResponse(
    long total,
    long waiting,
    long shipping,
    long delivered,
    long canceled
) {}