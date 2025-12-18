package com.nhnacademy._vidiafront.book.dto.books.gemini;

public record GeminiBookSuggestion(Long bookId,
                                   Integer rank,
                                   Double relevanceScore,
                                   boolean recommended,
                                   String summary) {
}
