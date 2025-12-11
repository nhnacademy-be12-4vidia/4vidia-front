package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBookApiClient {
    private static final String BOOK_SERVICE = "/api/v1/book-service";

    private final BackendApiClient backendApiClient;
}
