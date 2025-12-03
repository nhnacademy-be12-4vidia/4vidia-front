package com.nhnacademy._vidiafront.cart.client;

import com.nhnacademy._vidiafront.cart.dto.request.AddCartItemRequest;
import com.nhnacademy._vidiafront.cart.dto.request.CartUpdateBookRequest;
import com.nhnacademy._vidiafront.cart.dto.response.CartResponse;
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
    public void updateItem(Long bookId, CartUpdateBookRequest cartUpdateBookRequest){
        backendApiClient.put(CART_SERVICE + "/items/" + bookId, cartUpdateBookRequest, Void.class);
    }

    /**
     * 장바구니 도서 삭제
     */
    public void deleteItem(Long bookId){
        backendApiClient.delete(CART_SERVICE + "/items/" + bookId, Void.class);
    }

    /**
     * 장바구니 비우기
     */
    public void clearCart(){
        backendApiClient.delete(CART_SERVICE + "/items", Void.class);
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
    public void mergeGuestCartToUser(){
        backendApiClient.postNoBody(CART_SERVICE + "/merge-guest", Void.class);
    }

    /**
     * 비회원 장바구니 삭제 (팝업에서 no)
     */
    public void deleteGuestCart(){
        backendApiClient.delete(CART_SERVICE + "/guest", Void.class);
    }

    /**
     * 장바구니에 아이템 담기
     */
    public void addItem(AddCartItemRequest addCartItemRequest) {
        backendApiClient.post(CART_SERVICE + "/cart/items", addCartItemRequest, Void.class);
    }

}
