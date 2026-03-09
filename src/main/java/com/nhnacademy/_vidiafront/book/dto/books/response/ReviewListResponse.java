package com.nhnacademy._vidiafront.book.dto.books.response;

import java.time.LocalDate;
import java.util.List;

public record ReviewListResponse(

    Long reviewId,

    Long userId,
    String userName,

    String content,
    Integer rating,

    List<String> imageUrlList,
    LocalDate createdAt,

    boolean myReview,
    boolean modified


) {

}
