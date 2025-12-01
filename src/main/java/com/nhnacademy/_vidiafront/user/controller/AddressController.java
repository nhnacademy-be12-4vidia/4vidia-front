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

    // 도로명 찾기 팝업창
    @GetMapping("/jusoPopup")
    public String jusoPopup() {
        return "/mypage/address/jusoPopup";
    }

    @PostMapping("/jusoCallback")
    public String jusoCallback(@RequestParam Map<String, String> addressData, Model model) {
        model.addAttribute("jusoData", addressData);

        return "/mypage/address/jusoCallback";
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

        return "/mypage/address/addressList";
    }


    // 기본 주소 등록
    @PutMapping("/default/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void defaultAddress(@PathVariable Long addressId) {

        log.info("{}", addressId);
        String result = addressApiClient.updateDefaultAddress(addressId);
        log.info("Default address result: {}", result);
    }



    // 주소등록
    @PostMapping
    public String addAddress(CreateAddressRequest createAddressRequest) {
        String result = addressApiClient.addAddress(createAddressRequest);
        log.info("Register address result: {}", result);
        return "redirect:/mypage/address";
    }


    // 주소수정 form
    @GetMapping("/{addressId}/edit")
    public String getAddressForm(@PathVariable Long addressId, Model model) {


        AddressResponse addressResponse = addressApiClient.getAddress(addressId);
        model.addAttribute("address", addressResponse);

        return "/mypage/address/addressUpdate";
    }

    // 주소수정
    @PutMapping("/{addressId}")
    public String updateAddress(@PathVariable Long addressId,
                                AddressRequest addressRequest,
                                Model model) {
        AddressResponse addressResponse = addressApiClient.updateAddress(addressId, addressRequest);
        model.addAttribute("address", addressResponse);
        return "redirect:/mypage/address";
    }

    // 주소 삭제
    @DeleteMapping("/{addressId}")
    public String deleteAddress(@PathVariable Long addressId) {
        addressApiClient.deleteAddress(addressId);
//        log.info("Delete address result: {}", result);
        return "redirect:/mypage/address";
    }




}
