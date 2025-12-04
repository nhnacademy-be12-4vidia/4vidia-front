package com.nhnacademy._vidiafront.order.dto.order.request;

import java.util.List;

public record OrderCheckoutListRequest(
    List<OrderCheckoutRequest> items
) { }