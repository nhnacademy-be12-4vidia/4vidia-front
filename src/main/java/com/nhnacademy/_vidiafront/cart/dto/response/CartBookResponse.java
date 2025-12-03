package com.nhnacademy._vidiafront.cart.dto.response;

public record CartBookResponse(
       BookSummaryResponse book,
       Integer quantity
) { }

