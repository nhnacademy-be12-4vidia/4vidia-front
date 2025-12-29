package com.nhnacademy._vidiafront.point.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import com.nhnacademy._vidiafront.point.dto.request.PointOrderRewardRequest;
import com.nhnacademy._vidiafront.point.dto.request.PointPolicyRewardRequest;
import com.nhnacademy._vidiafront.point.dto.request.PointRefundRewardRequest;
import com.nhnacademy._vidiafront.point.dto.request.PointUseRequest;
import com.nhnacademy._vidiafront.point.dto.response.PointExpireSoon;
import com.nhnacademy._vidiafront.point.dto.response.PointHistoryPageResponse;
import com.nhnacademy._vidiafront.point.dto.response.PointTotalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor

public class PointApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";

    /**
     * 현재 보유 포인트 조회 (회원에서 가져옴)
     */
    public PointTotalResponse getPointTotal() {
        return backendApiClient.get(
                USER_SERVICE+"/users/me/points/remain",new ParameterizedTypeReference<>(){}
        );
    }

    /**
     * 소멸 예정 포인트 조회 ( 기본 7일 )
     */
    public PointExpireSoon getExpireSoon(int days){
        return backendApiClient.get(
                USER_SERVICE+"/users/me/points/expire-soon?days="+days,
                new ParameterizedTypeReference<>(){}        );
    }



    /**
     * 포인트 내역 조회 (기간 파라미터 포함)
     */
    public PointHistoryPageResponse getHistoryPage(String category, LocalDate from, LocalDate to, int page, int limit) {
        // 날짜를 명시적으로 String으로 변환하여 전달 (ISO_DATE 형식: 2025-12-24)
        String url = String.format("%s/users/me/points/history?category=%s&from=%s&to=%s&page=%d&size=%d",
                USER_SERVICE, category, from, to, page, limit);

        return backendApiClient.get(url, new ParameterizedTypeReference<>(){});
    }

    /**
     * 정책 적립 - 회원가입
     */
    public void rewardBySignUp(PointPolicyRewardRequest pointPolicyRewardRequest){
        backendApiClient.post(USER_SERVICE + "/points/signup", pointPolicyRewardRequest, new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

    /**
     * 정책 적립 - 리뷰
     */
    public void rewardByReview(PointPolicyRewardRequest pointPolicyRewardRequest){
        backendApiClient.post(USER_SERVICE + "/users/me/points/review", pointPolicyRewardRequest, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 주문 적립
     */

    public void rewardByOrder(PointOrderRewardRequest pointOrderRewardRequest){
        backendApiClient.post(USER_SERVICE + "/users/me/points/reward", pointOrderRewardRequest, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 환불 적립
     */
    public void rewardByRefund(PointRefundRewardRequest pointRefundRequest){
        backendApiClient.post(USER_SERVICE + "/users/me/points/refund", pointRefundRequest, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 주문 사용
     */
    public void usePoint(PointUseRequest pointUseRequest){
        backendApiClient.post(USER_SERVICE + "/users/me/points/use", pointUseRequest, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 포인트 취소
     */
    public void cancelUse(Long orderId){
        backendApiClient.postNoBody(USER_SERVICE + "/users/me/points/cancel?orderId=" + orderId, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }
}
