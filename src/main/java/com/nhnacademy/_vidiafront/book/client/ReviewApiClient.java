package com.nhnacademy._vidiafront.book.client;

import com.nhnacademy._vidiafront.book.dto.reviews.request.ReviewCreateRequest;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ReviewApiClient {

    private final BackendApiClient backendApiClient;
    private static final String BOOK_SERVICE = "/api/v1/book-service";

    public String createReview(ReviewCreateRequest request, List<MultipartFile> reviewImageList)
        throws IOException {

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("bookId", request.bookId());
        parts.add("orderItemId", request.orderItemId());
        parts.add("content", request.content());
        parts.add("rating", request.rating());

        if (reviewImageList != null) {
            for (MultipartFile file : reviewImageList) {
                if (file == null || file.isEmpty()) continue;

                ByteArrayResource resource = new ByteArrayResource(file.getBytes()){
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                };
                parts.add("images", resource);
            }
        }

        String uri = BOOK_SERVICE + "/books/" + request.bookId() + "/reviews";

        return backendApiClient.postMultipartFile(uri, parts, String.class);
    }
}
