package com.nhnacademy._vidiafront.admin.dto.response;

import java.time.LocalDate;
import java.util.List;

public record AdminBookResponse(
    Long id,
    String title,
    String isbn,
    List<String> authorNames,
    String publisherName,
    LocalDate publishedDate,
    Integer priceStandard,
    Integer priceSales,
    Integer stock,
    String status,
    String imageUrl
) {}
