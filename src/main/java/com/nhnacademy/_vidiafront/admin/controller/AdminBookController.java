package com.nhnacademy._vidiafront.admin.controller;

import com.nhnacademy._vidiafront.admin.client.AdminBookApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class AdminBookController {
    private final AdminBookApiClient bookApiClient;

    @GetMapping("/register")
    public String registerForm() {
        return "admin/admin-book-register";
    }
}
