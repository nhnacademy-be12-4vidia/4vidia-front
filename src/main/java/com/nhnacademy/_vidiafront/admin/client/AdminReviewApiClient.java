package com.nhnacademy._vidiafront.admin.client;

import com.nhnacademy._vidiafront.admin.dto.request.AdminReviewSearchRequest;
import com.nhnacademy._vidiafront.admin.dto.response.AdminReviewPageResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AdminReviewApiClient {

    private static final String REVIEW_SERVICE = "/api/v1/book-service";

    private final BackendApiClient  backendApiClient;

    /**
     * 관리자 리뷰 목록 조회 (검색 + 페이징)
     */

    public AdminReviewPageResponse getReviewPage(AdminReviewSearchRequest cond) {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(REVIEW_SERVICE).append("/admin/reviews");

        urlBuilder.append("?keyword=");
        if (cond.keyword() != null) {
            urlBuilder.append(cond.keyword());
        }

        urlBuilder.append("&page=").append(cond.pageOrDefault());
        urlBuilder.append("&size=").append(cond.sizeOrDefault());

        if(cond.ratingOrNull() != null){
            urlBuilder.append("&rating=").append(cond.ratingOrNull());
        }

        String url = urlBuilder.toString();

        return backendApiClient.get(
                url,
                new ParameterizedTypeReference<AdminReviewPageResponse>() {}
        );


    }


    /**
     * 관리자 리뷰 삭제
     */

    public void deleteReview (Long reviewId) {
        String url = REVIEW_SERVICE + "/admin/reviews/"+reviewId;
        backendApiClient.delete(url, Void.class);
    }


}
