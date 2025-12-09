package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.AdminUserSearchRequest;
import com.nhnacademy._vidiafront.admin.dto.request.UpdateUserStatusRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminUserPageResponse;
import com.nhnacademy._vidiafront.admin.dto.response.AdminUserResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AdminUserApiClient {
    private static final String USER_SERVICE = "/api/v1/user-service";

    private final BackendApiClient backendApiClient;

    /**
     * 관리자 회원 목록 조회 ( 검색 + 페이징 )
     */
    public AdminUserPageResponse getUserPage(AdminUserSearchRequest cond){
        var builder = UriComponentsBuilder
                .fromPath(USER_SERVICE + "/admin/users")
                .queryParam("page", cond.pageOrDefault())
                .queryParam("size", cond.sizeOrDefault());

        String keyword = cond.keywordOrNull();
        if (keyword != null) {
            builder.queryParam("keyword", keyword);   // ← 여기
        }

        String status = cond.statusOrNull();
        if (status != null) {
            builder.queryParam("status", status);
        }

        String url = builder.toUriString();

        return backendApiClient.get(url, new ParameterizedTypeReference<>(){});
    }

    /**
     * 단일 회원 상세 조회
     */
    public AdminUserResponse getUser(Long userId) {
        String url = USER_SERVICE + "/admin/users/"+userId;
        return backendApiClient.get(url, AdminUserResponse.class);
    }

    /**
     * 회원 상태 변경
     */
    public void updateUserStatus(Long userId, String status){
        String url = USER_SERVICE + "/admin/users/"+userId+"/status";
        UpdateUserStatusRequest body = new UpdateUserStatusRequest(status);
        backendApiClient.put(url, body, Void.class);
    }


}
