package com.nhnacademy._vidiafront.order.dto.order.request;

import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public record OrderCreateRequest(
        String recipientName,
        String addressRoadname,
        String addressDetail,
        String zipCode,
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "유효한 휴대폰 번호 형식(01X-XXXX-XXXX)이 아닙니다.")
        String recipientPhone,
        String deliveryRequest,
        LocalDate deliveryDate,
        String orderPassword,

        int totalPrice, //도서 가격 합
        int deliveryCost, //배송비
        int packagingCost, //포장비

        int couponDiscount, //쿠폰할인금액
        int pointUsed,  //포인트사용금액
        int payPrice, //도서가격 + 배송비 + 포장비 - 할인/포인트

        List<ItemRequestDto> orderItems,
        Long coupons
) {
    public record ItemRequestDto(
            long bookId,
            int quantity,
            int salePrice,
            List<Long> packagingOptionIds
    ) {}
}// order.html에서 넘어오는 값
