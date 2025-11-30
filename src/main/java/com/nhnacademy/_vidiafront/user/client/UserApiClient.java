package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.user.dto.request.UserSignupRequest;
import com.nhnacademy._vidiafront.user.dto.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class UserApiClient {
    private final RestClient restClient;
    private static final String USER_SERVICE = "/api/v1/user-service";

    // 테스트용
    private static final int TEST_ID = 6;
    private static final String X_USER_ID = "X-User-Id";

    /**
     * POST 회원가입 호출
     * */
    public String signup(UserSignupRequest userSignupRequest) {
        return restClient.post()
                .uri(USER_SERVICE + "/my/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .body(userSignupRequest)
                .retrieve()
                .body(String.class);
    }

    /**
     * GET 회원정보 조회 호출
     * */
    public UserProfileResponse getUserProfile() {
        return restClient.get()
                .uri(USER_SERVICE + "/my/profile")
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(UserProfileResponse.class);
    }







}
