package com.nhnacademy._vidiafront.cart.dto.response;

import java.util.List;

public record CartResponse(
        Long userId,
        List<CartBookResponse> items) {}

