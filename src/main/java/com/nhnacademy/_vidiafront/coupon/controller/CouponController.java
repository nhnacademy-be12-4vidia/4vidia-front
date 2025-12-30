package com.nhnacademy._vidiafront.coupon.controller;

import com.nhnacademy._vidiafront.coupon.client.CouponApiClient;
import com.nhnacademy._vidiafront.coupon.dto.response.MyCouponPageResponse;
import com.nhnacademy._vidiafront.coupon.dto.response.MyCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class CouponController {
    private final CouponApiClient couponApiClient;

    @GetMapping("/coupons")
    public String myCoupons(
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            Model model
    ) {
        MyCouponPageResponse res = couponApiClient.getMyCoupons(page, size, status);

        model.addAttribute("page", res.page()); // pagination용
        model.addAttribute("totalCount", res.totalCount());
        model.addAttribute("expireSoonCount", res.expireSoonCount());
        model.addAttribute("status", status);

        return "mypage/coupon/my-coupon-list";
    }


}
