package com.nhnacademy._vidiafront.admin.dto.response;

public record DeliveryResponse(
        Long orderId,
        String recipientName,
        String addressRoadname,
        String addressDetail,
        String recipientPhone,
        String deliveryStatus,
//        LocalDate deliveryDate,
        Integer payPrice,
//        LocalDateTime createdAt
        String orderStatus
) {
}
