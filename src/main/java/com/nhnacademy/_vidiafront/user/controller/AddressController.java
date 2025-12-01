package com.nhnacademy._vidiafront.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AddressController { // 도로명 찾기

    @GetMapping("/addressPopup")
    public String addressPopup() {
        return "/address/addressPopup";
    }

    @PostMapping("/addressCallback")
    public String addressCallback(@RequestParam Map<String, String> addressData, Model model) {
        model.addAttribute("addressData", addressData);

        return "/mypage/address/addressCallback";
    }
}
