package com.nhnacademy._vidiafront.book.dto.response;

import java.util.List;

public record BookListResponse(Long id, String title, String isbn, Integer priceSales, List<String> authorNames, String publisherName, String imageUrl) {

}
