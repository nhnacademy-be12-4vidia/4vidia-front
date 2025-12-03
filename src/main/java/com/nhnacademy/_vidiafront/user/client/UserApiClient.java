package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
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
    private final BackendApiClient backendApiClient;

    private static final String USER_SERVICE = "/api/v1/user-service";

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

}
