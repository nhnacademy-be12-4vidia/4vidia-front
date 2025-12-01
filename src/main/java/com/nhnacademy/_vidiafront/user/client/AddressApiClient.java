package com.nhnacademy._vidiafront.user.client;

import com.nhnacademy._vidiafront.user.dto.address.request.AddressRequest;
import com.nhnacademy._vidiafront.user.dto.address.request.CreateAddressRequest;
import com.nhnacademy._vidiafront.user.dto.address.response.AddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddressApiClient {
    private final RestClient restClient;

    private static final String USER_SERVICE = "/api/v1/user-service";

    // 테스트용
    private static final int TEST_ID = 8;
    private static final String X_USER_ID = "X-User-Id";


    /**
     * 주소 리스트 조회
     */
    public List<AddressResponse> getAddresseList() {
        return restClient.get()
                .uri(USER_SERVICE + "/my/addresses")
                .accept(MediaType.APPLICATION_JSON)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    /**
     * 주소 단일 조회
     */
    public AddressResponse getAddress(Long addressId) {
        return restClient.get()
                .uri(USER_SERVICE + "/my/addresses/" + addressId)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(AddressResponse.class);
    }


    // 기본주소 변경
    public String updateDefaultAddress(Long addressId) {
        return restClient.put()
                .uri(USER_SERVICE + "/my/addresses/change-default/" + addressId)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(String.class);
    }


    // 주소 추가
    public String addAddress(CreateAddressRequest createAddressRequest) {
        return restClient.post()
                .uri(USER_SERVICE + "/my/addresses")
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .body(createAddressRequest)
                .retrieve()
                .body(String.class);
    }

    // 주소 수정
    public AddressResponse updateAddress(Long addressId, AddressRequest addressRequest) {
        return restClient.put()
                .uri(USER_SERVICE + "/my/addresses/" + addressId)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .body(addressRequest)
                .retrieve()
                .body(AddressResponse.class);
    }

    // 주소 삭제
    public String deleteAddress(Long addressId) {
        return restClient.delete()
                .uri(USER_SERVICE + "/my/addresses/" + addressId)
                .header(X_USER_ID, String.valueOf(TEST_ID))
                .retrieve()
                .body(String.class);
    }

}
