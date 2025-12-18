package com.nhnacademy._vidiafront.coupon.controller;

import com.nhnacademy._vidiafront.coupon.client.CouponApiClient;
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
            Model model
    ) {
        var coupons = couponApiClient.getMyCoupons();

        // ✅ 상태 필터 (HTML 버튼용)
        List<MyCouponResponse> filteredCoupons =
                switch (status) {
                    case "UNUSED" ->
                            coupons.stream()
                                    .filter(c -> c.status().equals("UNUSED"))
                                    .toList();
                    case "USED" ->
                            coupons.stream()
                                    .filter(c -> c.status().equals("USED"))
                                    .toList();
                    case "EXPIRED" ->
                            coupons.stream()
                                    .filter(c -> c.status().equals("EXPIRED"))
                                    .toList();
                    default -> coupons;
                };

        long totalCount =
                coupons.stream()
                        .filter(c -> c.status().equals("UNUSED"))
                        .count();

        long expireSoonCount =
                coupons.stream()
                        .filter(c -> c.status().equals("UNUSED"))
                        .filter(c -> c.expireAt()
                                .isBefore(LocalDateTime.now().plusDays(7)))
                        .count();

        model.addAttribute("coupons", filteredCoupons);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("expireSoonCount", expireSoonCount);
        model.addAttribute("status", status);

        return "mypage/coupon/my-coupon-list";
    }


}
