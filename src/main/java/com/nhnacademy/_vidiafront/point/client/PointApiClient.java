package com.nhnacademy._vidiafront.point.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.point.dto.request.PointOrderRewardRequest;
import com.nhnacademy._vidiafront.point.dto.request.PointPolicyRewardRequest;
import com.nhnacademy._vidiafront.point.dto.request.PointRefundRewardRequest;
import com.nhnacademy._vidiafront.point.dto.request.PointUseRequest;
import com.nhnacademy._vidiafront.point.dto.response.PointExpireSoon;
import com.nhnacademy._vidiafront.point.dto.response.PointHistoryPageResponse;
import com.nhnacademy._vidiafront.point.dto.response.PointTotalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
                USER_SERVICE+"/my/points/remain",PointTotalResponse.class
        );
    }

    /**
     * 소멸 예정 포인트 조회 ( 기본 7일 )
     */
    public PointExpireSoon getExpireSoon(int days){
        return backendApiClient.get(
                USER_SERVICE+"/my/points/expire-soon?days="+days,
                PointExpireSoon.class
        );
    }


    /**
     * 포인트 내역 조회
     */
    public PointHistoryPageResponse getHistoryPage(int page, int limit) {
        return backendApiClient.get(
                USER_SERVICE+"/my/points/history?page="+page+"&size="+limit,
                PointHistoryPageResponse.class
        );
    }

    /**
     * 정책 적립
     */
    public void rewardByPolicy(PointPolicyRewardRequest pointPolicyRewardRequest){
        backendApiClient.post(USER_SERVICE + "/points/policy-reward", pointPolicyRewardRequest, Void.class);
    }

    /**
     * 주문 적립
     * @param pointOrderRewardRequest
     */
    public void rewardByOrder(PointOrderRewardRequest pointOrderRewardRequest){
        backendApiClient.post(USER_SERVICE + "/my/points/reward", pointOrderRewardRequest, Void.class);
    }

    /**
     * 환불 적립
     */
    public void rewardByRefund(PointRefundRewardRequest pointRefundRequest){
        backendApiClient.post(USER_SERVICE + "/my/points/refund", pointRefundRequest, Void.class);
    }

    /**
     * 주문 사용
     */
    public void usePoint(PointUseRequest pointUseRequest){
        backendApiClient.post(USER_SERVICE + "/my/points/use", pointUseRequest, Void.class);
    }

    /**
     * 포인트 취소
     */
    public void cancelUse(Long orderId){
        backendApiClient.postNoBody(USER_SERVICE + "/my/points/cancel?orderId=" + orderId, Void.class);
    }
}
