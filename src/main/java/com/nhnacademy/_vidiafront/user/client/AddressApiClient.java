package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import com.nhnacademy._vidiafront.user.dto.address.request.AddressRequest;
import com.nhnacademy._vidiafront.user.dto.address.request.CreateAddressRequest;
import com.nhnacademy._vidiafront.user.dto.address.response.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddressApiClient {
    private final BackendApiClient backendApiClient;
    private static final String USER_SERVICE = "/api/v1/user-service";
    private static final String BASE_URL = "/users/me/addresses";
    // 기존 base url = "/my/addresses" -> 수정 "/users/me/addresses"

    /**
     * 주소 등록
     * 기존 "/my/addresses"
     */
    public void addAddress(CreateAddressRequest createAddressRequest) {
        backendApiClient.post(USER_SERVICE + BASE_URL, createAddressRequest, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 주소 단일 조회
     * 기존 "/my/addresses/" + addressId
     */
    public AddressResponse getAddress(Long addressId) {
        return backendApiClient.get(USER_SERVICE + BASE_URL + "/" + addressId, new ParameterizedTypeReference<>() {});
    }

    /**
     * 주소 전체 조회
     * 기존 "/my/addresses"
     */
    public List<AddressResponse> getAddresseList() {
        return backendApiClient.get(USER_SERVICE + BASE_URL, new ParameterizedTypeReference<>() {});
    }

    /**
     * 주소 수정
     * 기존 "/my/addresses/" + addressId
     */
    public void updateAddress(Long addressId, AddressRequest addressRequest) {
        backendApiClient.put(USER_SERVICE + BASE_URL + "/" + addressId, addressRequest, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 기본주소 변경
     * 기존 "/my/addresses/change-default/" + addressId
     */
    public void updateDefaultAddress(Long addressId) {
        backendApiClient.putNoBody(USER_SERVICE + BASE_URL + "/" + addressId + "/default", new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    /**
     * 주소 삭제
     * 기존 "/my/addresses/" + addressId
     */
    public void deleteAddress(Long addressId) {
        backendApiClient.delete(USER_SERVICE + BASE_URL + "/" + addressId, new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

}
