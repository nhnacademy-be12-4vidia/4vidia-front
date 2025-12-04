package com.nhnacademy._vidiafront.point.dto.request;

public record PointRefundRewardRequest(
        Long orderId,
        int amount
){
}
