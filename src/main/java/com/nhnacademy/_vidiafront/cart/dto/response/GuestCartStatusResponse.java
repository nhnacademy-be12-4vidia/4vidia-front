package com.nhnacademy._vidiafront.cart.dto.response;

public record GuestCartStatusResponse(
        boolean hasGuestCart,
        int itemCount
) {}
