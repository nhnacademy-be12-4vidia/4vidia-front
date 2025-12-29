package com.nhnacademy._vidiafront.cart.client;

import com.nhnacademy._vidiafront.cart.dto.request.AddCartItemRequest;
import com.nhnacademy._vidiafront.cart.dto.request.CartUpdateBookRequest;
import com.nhnacademy._vidiafront.cart.dto.response.CartResponse;
import com.nhnacademy._vidiafront.cart.dto.response.GuestCartStatusResponse;
import com.nhnacademy._vidiafront.global.client.BackendApiClient;
import com.nhnacademy._vidiafront.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartApiClient {
    private final BackendApiClient backendApiClient;
    private static final String CART_SERVICE = "/api/v1/cart-service";

    /**
     * 장바구니 조회
     */
    public CartResponse getCart(){
        return backendApiClient.get(CART_SERVICE + "/cart", new ParameterizedTypeReference<>(){});
    }

    /**
     * 장바구니 도서 수량 수정
     */
    public void updateItem(Long bookId, CartUpdateBookRequest cartUpdateBookRequest){
        backendApiClient.put(CART_SERVICE + "/cart/items/" + bookId, cartUpdateBookRequest, new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

    /**
     * 장바구니 도서 단권 삭제
     */
    public void deleteItem(Long bookId){
        backendApiClient.delete(CART_SERVICE + "/cart/items/" + bookId, new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

    /**
     * 장바구니 도서 삭제
     */
    public void deleteItems(List<Long> bookIds){
        var builder = UriComponentsBuilder
                .fromUriString(CART_SERVICE + "/cart/items");

        bookIds.forEach(id -> builder.queryParam("itemIds", id.toString()));

        String url = builder.build().toUriString();

        backendApiClient.delete(url, new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

    /**
     * 비회원 장바구니 상태 조회
     */
    public GuestCartStatusResponse getGuestCartStatus(){
        return backendApiClient.get(CART_SERVICE + "/cart/guest/status", new ParameterizedTypeReference<>(){});
    }

    /**
     * 비회원 -> 회원 머지 (팝업에서 yes)
     */
    public void mergeGuestCartToUser(){
        backendApiClient.postNoBody(CART_SERVICE + "/cart/merge-guest", new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

    /**
     * 비회원 장바구니 삭제 (팝업에서 no)
     */
    public void deleteGuestCart(){
        backendApiClient.delete(CART_SERVICE + "/cart/guest", new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

    /**
     * 장바구니에 아이템 담기
     */
    public void addItem(AddCartItemRequest addCartItemRequest) {
        backendApiClient.post(CART_SERVICE + "/cart/items", addCartItemRequest, new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

    /**
     * 로그인 직후 호출
     */
    public void loginSync(){
        backendApiClient.postNoBody(CART_SERVICE+"/cart/login-sync", new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

    /**
     * 로그아웃 직후 호출
     */
    public void logoutSync(){
        backendApiClient.postNoBody(CART_SERVICE+"/cart/logout-sync", new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

    /**
     * 회원 탈퇴
     */
    public void deleteCart(){
        backendApiClient.delete(CART_SERVICE+"/cart", new ParameterizedTypeReference<ApiResponse<Void>>(){});
    }

}
