package com.nhnacademy._vidiafront.cart.dto.request;

public record AddCartItemRequest(
        Long bookId,
        Integer quantity) {
}
