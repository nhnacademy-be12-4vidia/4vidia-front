package com.nhnacademy._vidiafront.order.dto.order.request;

import java.util.List;

public record OrderPageRequest(
        Long bookId,
        Integer quantity,
        List<OrderCheckoutRequest> orderCheckoutRequests
) { }
