package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import com.nhnacademy._vidiafront.global.dto.PageResponse;
import com.nhnacademy._vidiafront.user.dto.like.response.LikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";
    private static final String BASE_URL = "/users/me/likes";

    /**
     * GET 좋아요 리스트 조회 (my page)
     * */
    public PageResponse<LikeResponse> getLikeListPage(Pageable pageable) {
        String uri = UriComponentsBuilder.fromUriString(USER_SERVICE + BASE_URL)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .toUriString();
        return backendApiClient.get(uri, new ParameterizedTypeReference<>() {});
    }

    /**
     * GET 좋아요 리스트 조회
     * */
    public List<LikeResponse> getLikeList() {
        return backendApiClient.get(USER_SERVICE + BASE_URL + "/all", new ParameterizedTypeReference<>() {});
    }

    /**
     * POST 좋아요 등록
     */
    public void addLike(Long bookId) {
        backendApiClient.postNoBody(USER_SERVICE + BASE_URL + "/" + bookId, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * DELETE 좋아요 삭제
     */
    public void deleteLike(Long bookId) {
        backendApiClient.delete(USER_SERVICE + BASE_URL + "/" + bookId, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * DELETE 좋아요 전체 삭제
     */
    public void deleteAllLikes() {
        backendApiClient.delete(USER_SERVICE + BASE_URL, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }
}
