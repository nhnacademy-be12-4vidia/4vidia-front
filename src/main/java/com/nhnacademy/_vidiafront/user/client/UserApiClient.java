package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.user.dto.user.request.ChangePasswordRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.DeleteUserRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UpdateLastLoginRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UpdateUserRequest;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserApiClient {
    private final BackendApiClient backendApiClient;

    private static final String USER_SERVICE = "/api/v1/user-service";

    // 회원 이름 조회
    public String getUserName() {
        return backendApiClient.get(USER_SERVICE + "/my/name", String.class);
    }

    /**
     * 회원정보 조회
     * */
    public UserProfileResponse getUserProfile() {
        return backendApiClient.get(USER_SERVICE + "/my/profile", UserProfileResponse.class);
    }

    /**
     * 회원정보 수정
     * */
    public UserProfileResponse updateUserProfile(UpdateUserRequest updateUserRequest) {
        return backendApiClient.put(USER_SERVICE + "/my/profile",updateUserRequest, UserProfileResponse.class);
    }

    /**
     * 비밀번호 수정
     * */
    public Void changePassword(ChangePasswordRequest changePasswordRequest) {
        return backendApiClient.put(USER_SERVICE + "/my/change-password", changePasswordRequest, Void.class);
    }


    /**
     * 회원탈퇴
     */
    public Void deleteUser(DeleteUserRequest deleteUserRequest) {
        return backendApiClient.put(USER_SERVICE + "/my/delete", deleteUserRequest, Void.class);
    }

    // 마지막로그인시간 업데이트하기
    public Void updateLastLoginAt(String email) {
        UpdateLastLoginRequest updateLastLoginRequest = new UpdateLastLoginRequest(email);
        return backendApiClient.put(USER_SERVICE + "/auth/update-time", updateLastLoginRequest, Void.class);
    }
}
