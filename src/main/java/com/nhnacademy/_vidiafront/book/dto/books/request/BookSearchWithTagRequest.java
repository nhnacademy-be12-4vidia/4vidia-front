package com.nhnacademy._vidiafront.book.dto.books.request;

import java.util.List;

public record BookSearchWithTagRequest(
        List<String> tagNameList,
        MatchMode mode

) {public enum MatchMode {AND, OR}}
