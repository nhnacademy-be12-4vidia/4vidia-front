package com.nhnacademy._vidiafront.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage/point")
public class PointController {

    @GetMapping
    public String pointHistory() {
        return "mypage/point/pointHistory";
    }
}
