package com.nhnacademy._vidiafront.book.dto.reviews.request;

public record ReviewCreateRequest(
    Long bookId,
    Long orderItemId,
    String content,
    Integer rating) {

}
