package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.global.client.BackendApiClient;
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

    /**
     * 주소 리스트 조회
     */
    public List<AddressResponse> getAddresseList() {
        return backendApiClient.get(USER_SERVICE + "/my/addresses", new ParameterizedTypeReference<>() {});
    }
    /**
     * 주소 단일 조회
     */
    public AddressResponse getAddress(Long addressId) {
        return backendApiClient.get(USER_SERVICE + "/my/addresses/" + addressId,AddressResponse.class);
    }

    // 기본주소 변경
    public String updateDefaultAddress(Long addressId) {
        return backendApiClient.putNoBody(USER_SERVICE + "/my/addresses/change-default/" + addressId, String.class);
    }


    // 주소 추가
    public String addAddress(CreateAddressRequest createAddressRequest) {
        return backendApiClient.post(USER_SERVICE + "/my/addresses", createAddressRequest, String.class);
    }

    // 주소 수정
    public AddressResponse updateAddress(Long addressId, AddressRequest addressRequest) {
        return backendApiClient.put(USER_SERVICE + "/my/addresses/" + addressId, addressRequest, AddressResponse.class);
    }

    // 주소 삭제
    public String deleteAddress(Long addressId) {
        return backendApiClient.delete(USER_SERVICE + "/my/addresses/" + addressId, String.class);
    }

}
