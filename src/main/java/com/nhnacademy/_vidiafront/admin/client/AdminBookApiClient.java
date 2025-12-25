package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.AdminBookCreateRequest;
import com.nhnacademy._vidiafront.admin.dto.request.AdminBookUpdateRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminIsbnSearchResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AdminBookApiClient {
    private static final String BOOK_SERVICE = "/api/v1/book-service";

    private final BackendApiClient backendApiClient;

    public void createBook(AdminBookCreateRequest request) {
        String url = BOOK_SERVICE + "/admin/books";
        backendApiClient.post(url, request, Void.class);
    }

    public void updateBook(Long bookId, AdminBookUpdateRequest request) {
        String url = BOOK_SERVICE + "/admin/books/" + bookId;
        backendApiClient.put(url, request, Void.class);
    }

    public AdminIsbnSearchResponse searchBookByIsbn(String isbn) {
        String url = BOOK_SERVICE + "/admin/books/search?isbn=" + isbn;
        return backendApiClient.get(url, AdminIsbnSearchResponse.class);
    }

    public AdminIsbnSearchResponse getAugmentedBookInfo(String isbn) {
        String url = BOOK_SERVICE + "/admin/books/augment?isbn=" + isbn;
        return backendApiClient.get(url, AdminIsbnSearchResponse.class);
    }

    public String uploadImage(MultipartFile image) throws IOException {
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

        String url = BOOK_SERVICE + "/admin/images/upload";
        return backendApiClient.postMultipartFile(url, parts, String.class);
    }
}

