package com.nhnacademy._vidiafront.book.dto.books.response;

import java.time.LocalDate;
import java.util.List;

public record BookDetailResponse(
    Long id,
    String isbn,
    String title,
    String subtitle,
    String bookIndex,
    String description,

    PublisherInfo publisher,
    LocalDate publishedDate,

    CategoryInfo category,

    Integer pageCount,
    String language,
    Integer priceStandard,
    Integer priceSales,
    Integer stock,
    String stockStatus,
    boolean packagingAvailable,

    List<AuthorInfo> authors,
    Integer volumeNumber,
    List<String> imageUrls,
    List<String> tags,

    Long reviewCount,
    Double avgRating
) {

    public record PublisherInfo(
        Long id,
        String name
    ) {}

    public record AuthorInfo(
        Long id,
        String name,
        String role
    ) {}

    public record CategoryInfo(
        Long id,
        String name,
        String kdcCode
    ) {}

}
