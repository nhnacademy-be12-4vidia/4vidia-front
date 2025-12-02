package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.user.dto.user.request.ChangePasswordRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.DeleteUserRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UpdateUserRequest;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
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
    private static final int TEST_ID = 1;
    private static final String X_USER_ID = "X-User-Id";

    /**
     * 회원정보 조회
     * */
    public UserProfileResponse getUserProfile() {
        return restClient.get()
                .uri(USER_SERVICE + "/my/profile")
                .accept(MediaType.APPLICATION_JSON)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(UserProfileResponse.class);
    }

    /**
     * 회원정보 수정
     * */
    public UserProfileResponse updateUserProfile(UpdateUserRequest updateUserRequest) {
        return restClient.put()
                .uri(USER_SERVICE + "/my/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .body(updateUserRequest)
                .retrieve()
                .body(UserProfileResponse.class);
    }

    /**
     * 비밀번호 수정
     * */
    public Void changePassword(ChangePasswordRequest changePasswordRequest) {
        return restClient.put()
                .uri(USER_SERVICE + "/my/change-password")
                .contentType(MediaType.APPLICATION_JSON) // todo : 비밀번호가 body로 넘어가도 괜찮을까?
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .body(changePasswordRequest)
                .retrieve()
                .body(Void.class);
    }


    /**
     * 회원탈퇴
     */
    public Void deleteUser(DeleteUserRequest deleteUserRequest) {
        return restClient.put()
                .uri(USER_SERVICE + "/my/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .body(deleteUserRequest)
                .retrieve()
                .body(Void.class);
    }

}
