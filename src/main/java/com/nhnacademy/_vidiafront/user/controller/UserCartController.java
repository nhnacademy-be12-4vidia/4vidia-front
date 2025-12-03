package com.nhnacademy._vidiafront.user.controller;

import com.nhnacademy._vidiafront.cart.client.CartApiClient;
import com.nhnacademy._vidiafront.cart.dto.request.AddCartItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class UserCartController {
    private final CartApiClient cartApiClient;
    // todo : 장바구니 실제로 담아야됨, 요청보내기?

    @PostMapping("/cart/add")
    public String addCartItem(@RequestParam("bookId") Long bookId,
                              @RequestParam(value = "quantity", defaultValue = "1") Integer quantity) {

        AddCartItemRequest cartItemAddRequest = new AddCartItemRequest(bookId, quantity);

        cartApiClient.addItem(cartItemAddRequest);

        return "redirect:/cart";
    }
}
