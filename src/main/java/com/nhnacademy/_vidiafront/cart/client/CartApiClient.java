package com.nhnacademy._vidiafront.cart.client;

import com.nhnacademy._vidiafront.cart.dto.request.CartUpdateBookRequest;
import com.nhnacademy._vidiafront.cart.dto.response.CartResponse;
import com.nhnacademy._vidiafront.cart.dto.response.CartUpdateBookResponse;
import com.nhnacademy._vidiafront.cart.dto.response.GuestCartStatusResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartApiClient {
    private final BackendApiClient backendApiClient;
    private static final String CART_SERVICE = "/api/v1/cart-service";

    /**
     * 장바구니 조회
     */
    public CartResponse getCart(){
        return backendApiClient.get(CART_SERVICE + "/cart", CartResponse.class);
    }

    /**
     * 장바구니 도서 수량 수정
     */
    // TODO putNoBody : Request 넣을 수 있게 메서드 추가해주시면 안되는건가여......
    public CartUpdateBookResponse updateItem(Long bookId, CartUpdateBookRequest cartUpdateBookRequest){
        return backendApiClient.put(CART_SERVICE + "/items/" + bookId, cartUpdateBookRequest, CartUpdateBookResponse.class);
    }

    /**
     * 장바구니 도서 삭제
     */
    public String deleteItem(Long bookId){
        return backendApiClient.delete(CART_SERVICE + "/items/" + bookId, String.class);
    }

    /**
     * 장바구니 비우기
     */
    public String clearCart(){
        return backendApiClient.delete(CART_SERVICE + "/items", String.class);
    }

    /**
     * 비회원 장바구니 상태 조회
     */
    public GuestCartStatusResponse getGuestCartStatus(){
        return backendApiClient.get(CART_SERVICE + "/guest/status", GuestCartStatusResponse.class);
    }

    /**
     * 비회원 -> 회원 머지 (팝업에서 yes)
     */
    public String mergeGuestCartToUser(){
        return backendApiClient.postNoBody(CART_SERVICE + "/merge-guest", String.class);
    }

    /**
     * 비회원 장바구니 삭제 (팝업에서 no)
     */
    public String deleteGuestCart(){
        return backendApiClient.delete(CART_SERVICE + "/guest", String.class);
    }
}
