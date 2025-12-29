package com.nhnacademy._vidiafront.book.client;

import com.nhnacademy._vidiafront.book.dto.reviews.request.ReviewCreateRequest;
import com.nhnacademy._vidiafront.book.dto.reviews.request.ReviewUpdateRequest;
import com.nhnacademy._vidiafront.book.dto.reviews.response.ReviewListWithSummaryResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

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

        return backendApiClient.postMultipartFile(uri, parts, new ParameterizedTypeReference<>(){});
    }

    public ReviewListWithSummaryResponse getReviewsWithSummary(Long bookId, int page, int size) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromPath(BOOK_SERVICE + "/books/" + bookId + "/reviews")
                .queryParam("page", page)
                .queryParam("size", size);

        return backendApiClient.get(uriBuilder.toUriString(), new ParameterizedTypeReference<>(){});
    }

    public void deactivateReview(Long reviewId, Long bookId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(BOOK_SERVICE + "/books/" + bookId + "/reviews/" + reviewId + "/deactivate");

        backendApiClient.postNoBody(uriBuilder.toUriString(), new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    public void editReview(ReviewUpdateRequest request, List<MultipartFile> newImageList) throws IOException {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("bookId", request.getBookId());
        parts.add("content", request.getContent());
        parts.add("rating", request.getRating());

        if (newImageList != null) {
            for (MultipartFile file : newImageList) {
                if (file == null || file.isEmpty()) continue;

                ByteArrayResource resource = new ByteArrayResource(file.getBytes()){
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();}
                };
                parts.add("images", resource);
            }
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(BOOK_SERVICE + "/books/" + request.getBookId() + "/reviews/" + request.getReviewId() + "/edit");

        backendApiClient.postMultipartFile(uriBuilder.toUriString(), parts,  new ParameterizedTypeReference<ApiResponse<Void>>() {});

    }

    public void deactivateReview(Long reviewId, Long bookId) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(BOOK_SERVICE + "/books/" + bookId + "/reviews/" + reviewId + "/deactivate");

        backendApiClient.postNoBody(uriBuilder.toUriString(), Void.class);
    }

    public void editReview(ReviewUpdateRequest request, List<MultipartFile> newImageList) throws IOException {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("bookId", request.getBookId());
        parts.add("content", request.getContent());
        parts.add("rating", request.getRating());

        if (newImageList != null) {
            for (MultipartFile file : newImageList) {
                if (file == null || file.isEmpty()) continue;

                ByteArrayResource resource = new ByteArrayResource(file.getBytes()){
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();}
                };
                parts.add("images", resource);
            }
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(BOOK_SERVICE + "/books/" + request.getBookId() + "/reviews/" + request.getReviewId() + "/edit");

        backendApiClient.postMultipartFile(uriBuilder.toUriString(), parts, Void.class);

    }
}
