package com.nhnacademy._vidiafront.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @GetMapping("/register")
    public String registerForm() {
        return "admin/book-register";
    }
}
