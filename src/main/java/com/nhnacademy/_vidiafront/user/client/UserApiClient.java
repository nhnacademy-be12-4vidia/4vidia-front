package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.user.dto.auth.request.CompleteProfileRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.ChangePasswordRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.DeleteUserRequest;
import com.nhnacademy._vidiafront.user.dto.user.request.UpdateUserRequest;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";
    private static final String BASE_URL = "/users";
    // 기존 base url = "/my" -> 수정 "/users"

    /**
     * 회원 이름 조회
     * 기존 "/my/name"
     */
    public String getUserName() {
        return backendApiClient.get(USER_SERVICE + BASE_URL + "/name", new ParameterizedTypeReference<>() {});
    }

    /**
     * 회원정보 조회
     * 기존 "/my/profile"
     * */
    public UserProfileResponse getUserProfile() {
        return backendApiClient.get(USER_SERVICE + BASE_URL + "/profile", new ParameterizedTypeReference<>() {});
    }

    /**
     * 회원정보 수정
     * 기존 "/my/profile"
     * */
    public UserProfileResponse updateUserProfile(UpdateUserRequest updateUserRequest) {
        return backendApiClient.put(USER_SERVICE + BASE_URL + "/profile",updateUserRequest, new ParameterizedTypeReference<>() {});
    }

    /**
     * 비밀번호 수정
     * 기존 "/my/change-password"
     * */
    public Void changePassword(ChangePasswordRequest changePasswordRequest) {
        return backendApiClient.put(USER_SERVICE + BASE_URL + "/me/password", changePasswordRequest, new ParameterizedTypeReference<>() {});
    }

    /**
     * 회원탈퇴
     * 기존 "/my/delete"
     */
    public Void deleteUser(DeleteUserRequest deleteUserRequest) {
        return backendApiClient.put(USER_SERVICE + BASE_URL + "/delete", deleteUserRequest, new ParameterizedTypeReference<>() {});
    }

    public String getUserRole() {
        return backendApiClient.get(USER_SERVICE + BASE_URL + "/role", new ParameterizedTypeReference<>() {});
    }

    /**
     * payco 로그인 필수 정보 가입
     */
    public void completeProfile(CompleteProfileRequest completeProfileRequest) {
        backendApiClient.put(USER_SERVICE + BASE_URL + "/complete-profile", completeProfileRequest, new ParameterizedTypeReference<>() {});
    }

    // 기존 마지막로그인시간 업데이트 auth api client로 이동시킴

}
