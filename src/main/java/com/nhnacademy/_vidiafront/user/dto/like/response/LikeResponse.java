package com.nhnacademy._vidiafront.user.dto.like.response;

public record LikeResponse(
        Long bookId,
        String bookTitle, // 책 제목
        String authorName, // 저자
        Integer priceStandard, // 판매가
        Integer priceSales, // 할인가
        String stockStatus, // 재고상태(품절인지)
        String bookImage // 책 이미지
){
}