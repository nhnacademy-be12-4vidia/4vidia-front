package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.AdminBookCreateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.AdminBookUpdateRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminIsbnSearchResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminBookApiClient {
    private static final String BOOK_SERVICE = "/api/v1/book-service/admin/books";

    private final BackendApiClient backendApiClient;

    public void createBook(AdminBookCreateRequest request, MultipartFile thumbnail) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        // JSON 파트 준비
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AdminBookCreateRequest> requestEntity = new HttpEntity<>(request, jsonHeaders);

        // 파트 담기
        parts.add("request", requestEntity);
        parts.add("thumbnail", thumbnail.getResource());

        backendApiClient.postMultipartFile(BOOK_SERVICE, parts, Void.class);
    }

    public void updateBook(Long bookId, AdminBookUpdateRequest request, MultipartFile thumbnail) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        // JSON 파트 준비
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AdminBookUpdateRequest> requestEntity = new HttpEntity<>(request, jsonHeaders);

        // 파트 담기
        parts.add("request", requestEntity);
        parts.add("thumbnail", thumbnail.getResource());

        String url = BOOK_SERVICE + "/" + bookId;
        backendApiClient.postMultipartFile(url, parts, Void.class);
    }

    public AdminIsbnSearchResponse searchBookByIsbn(String isbn) {
        String url = BOOK_SERVICE + "/search?isbn=" + isbn;
        return backendApiClient.get(url, AdminIsbnSearchResponse.class);
    }

    public AdminIsbnSearchResponse getAugmentedBookInfo(String isbn) {
        String url = BOOK_SERVICE + "/augment?isbn=" + isbn;
        return backendApiClient.get(url, AdminIsbnSearchResponse.class);
    }

    public String getBookIsbn(Long bookId) {
        String url = BOOK_SERVICE + "/" + bookId + "/isbn";
        return backendApiClient.get(url, String.class);
    }

    public Map<String, String> uploadImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null;
        }

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        ByteArrayResource resource = new ByteArrayResource(image.getBytes()){
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };
        parts.add("image", resource);

        String url = BOOK_SERVICE + "/images";
        return backendApiClient.postMultipartFile(url, parts, Map.class);
    }
}

