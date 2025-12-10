package com.nhnacademy._vidiafront.admin.dto.response;

import com.nhnacademy._vidiafront.order.dto.DeliveryStatus;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DeliveryResponse(
        // list 화면에 출력할 정보
        long orderId,
        String email,
        String recipientName,
        String addressRoadname,
        String addressDetail,
        DeliveryStatus deliveryStatus,

        // 상세 추가
        int payPrice,
        String recipientPhone,
        String deliveryRequest,
        LocalDateTime createAt,
        int couponDiscount,
        int pointUsed,
        LocalDate deliveryDate,
        LocalDate actualDeliveryDate,
        List<OrderResponse.OrderBookResponse> orderItems
){}
