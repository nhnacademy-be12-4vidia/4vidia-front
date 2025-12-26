package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.user.client.AddressApiClient;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.address.request.AddressRequest;
import com.nhnacademy._vidiafront.user.dto.address.request.CreateAddressRequest;
import com.nhnacademy._vidiafront.user.dto.address.response.AddressResponse;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/mypage/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressApiClient addressApiClient;
    private final UserApiClient userApiClient;

    // todo : jusoPopup(), jusoCallback() 여기에 있어도 되나? /mypage/.. 로 시작하는데?? 주문에서도 쓰지않나??
    // 도로명 찾기 팝업창
    @GetMapping("/jusoPopup")
    public String jusoPopup() {
        return "mypage/address/jusoPopup";
    }

    @PostMapping("/jusoCallback")
    public String jusoCallback(@RequestParam Map<String, String> addressData, Model model) {
        model.addAttribute("jusoData", addressData);
        return "mypage/address/jusoCallback";
    }

    // --------------------------------------------------------

    /**
     * 주소 리스트 폼
     */
    @GetMapping
    public String addressList(Model model) {
        List<AddressResponse> addresseList = addressApiClient.getAddresseList();
        model.addAttribute("addressList", addresseList);

        UserProfileResponse userProfile = userApiClient.getUserProfile();
        model.addAttribute("defaultAddress", userProfile.defaultAddress());

        return "mypage/address/addressList";
    }

    /**
     * 기본 주소 등록
     */
    @PutMapping("/default/{addressId}")
    @ResponseBody
    public ResponseEntity<Void> defaultAddress(@PathVariable Long addressId) {
        addressApiClient.updateDefaultAddress(addressId);
        return ResponseEntity.ok().build();
    }

    /**
     * 주소등록
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> addAddress(CreateAddressRequest createAddressRequest) {
        addressApiClient.addAddress(createAddressRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 주소수정 form
     */
    @GetMapping("/{addressId}/edit")
    public String getAddressForm(@PathVariable Long addressId, Model model) {
        AddressResponse addressResponse = addressApiClient.getAddress(addressId);
        model.addAttribute("address", addressResponse);
        return "mypage/address/addressUpdate";
    }

    /**
     * 주소수정
     */
    @PutMapping("/{addressId}")
    public String updateAddress(@PathVariable Long addressId,
                                AddressRequest addressRequest) {
        addressApiClient.updateAddress(addressId, addressRequest);
        return "redirect:/mypage/address";
    }

    /**
     * 주소 삭제
     */
    @DeleteMapping("/{addressId}")
    @ResponseBody
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressApiClient.deleteAddress(addressId);
        return ResponseEntity.ok().build();
    }

}
