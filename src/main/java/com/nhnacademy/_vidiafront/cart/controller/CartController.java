package com.nhnacademy._vidiafront.cart.controller;

import com.nhnacademy._vidiafront.cart.client.CartApiClient;
import com.nhnacademy._vidiafront.cart.dto.request.AddCartItemRequest;
import com.nhnacademy._vidiafront.cart.dto.request.CartUpdateBookRequest;
import com.nhnacademy._vidiafront.cart.dto.response.CartResponse;
import com.nhnacademy._vidiafront.cart.dto.response.GuestCartStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartApiClient cartApiClient;

    // 장바구니 화면
    @GetMapping
    public String viewCart(Model model
    ) {
        CartResponse cart = cartApiClient.getCart();

        boolean loggedIn = (cart.userId()!= null);

        model.addAttribute("cart", cart);
        model.addAttribute("loggedIn", loggedIn);

        return "cart/cart";
    }

    /**
     * 장바구니 수량 변경 - 프론트용 AJAX 엔드포인트
     */
    @PutMapping("/items/{bookId}")
    @ResponseBody
    public ResponseEntity<Void> updateCartItem(
            @PathVariable Long bookId,
            @RequestBody CartUpdateBookRequest request
    ) {
        // 여기서 Feign 클라이언트로 "진짜 백엔드(cart-service)" 호출
        cartApiClient.updateItem(bookId, request);
        return ResponseEntity.ok().build();
    }


    // 도서 삭제
    @DeleteMapping("/items/{bookId}")
    public String deleteItem(@PathVariable Long bookId) {
        cartApiClient.deleteItem(bookId);
        return "redirect:/cart";
    }

    // 장바구니 비우기
    @PostMapping("/items/clear")
    public String clearCart() {
        cartApiClient.clearCart();
        return "redirect:/cart";
    }

    // 비회원 장바구니 상태 조회 (로그인 상태 + 게스트 장바구니 있을 때만 모달)
    // TODO 로그인 상태일때만 부르도록 수정 필요
    @GetMapping("/guest/status")
    @ResponseBody
    public GuestCartStatusResponse guestCartStatus() {
        return cartApiClient.getGuestCartStatus();
    }

    // 🔹 "예" 선택: 비회원 → 회원 장바구니 머지
    @PostMapping("/merge-guest")
    @ResponseBody
    public void mergeGuestCart() {
        cartApiClient.mergeGuestCartToUser();
    }

    // 🔹 "아니오" 선택: 비회원 장바구니만 삭제
    @DeleteMapping("/guest")
    @ResponseBody
    public void clearGuestCart(){
        cartApiClient.deleteGuestCart();
    }


    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addCartItem(@RequestParam("bookId") Long bookId,
                              @RequestParam(value = "quantity", defaultValue = "1") Integer quantity) {

        AddCartItemRequest cartItemAddRequest = new AddCartItemRequest(bookId, quantity);

        cartApiClient.addItem(cartItemAddRequest);

        return ResponseEntity.ok("ok");
    }
}
