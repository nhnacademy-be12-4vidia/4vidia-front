package com.nhnacademy._vidiafront.user.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AddressApiClient {
    private final RestClient restClient;

    private static final String USER_SERVICE = "/api/v1/user-service";

    // 테스트용
    private static final int TEST_ID = 8;
    private static final String X_USER_ID = "X-User-Id";
}
