package com.nhnacademy._vidiafront.order.controller;

import com.nhnacademy._vidiafront.coupon.client.CouponApiClient;
import com.nhnacademy._vidiafront.coupon.dto.OrderCouponResponse;
import com.nhnacademy._vidiafront.global.auth.LoginStatus;
import com.nhnacademy._vidiafront.order.client.OrderApiClient;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCheckoutRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCreateRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderPageRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderTrackingRequest;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCheckoutResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCreateResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderResponse;
import com.nhnacademy._vidiafront.user.client.MyOrderApiClient;
import com.nhnacademy._vidiafront.user.dto.user.response.OrderUserResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApiClient orderApiClient;
    private final MyOrderApiClient myOrderApiClient;
    private final CouponApiClient couponApiClient;
    private final LoginStatus loginStatus;

    // 바로구매, 장바구니에서 선택된 도서 임시 저장 후 결제 전 주문 확인 페이지로
    @PostMapping("/checkout-temp")
    public String checkoutTemp(@ModelAttribute OrderPageRequest orderPageRequest) {
        List<OrderCheckoutRequest> orderCheckoutRequests;

        if (orderPageRequest.orderCheckoutRequests() != null) {
            orderCheckoutRequests = orderPageRequest.orderCheckoutRequests();
        } else {
            orderCheckoutRequests = List.of(OrderCheckoutRequest.from(orderPageRequest.bookId(), orderPageRequest.quantity()));
        }

        String orderKey = orderApiClient.saveTempOrderCheckout(orderCheckoutRequests);

        return "redirect:/orders?key=" + orderKey;
    }

    @GetMapping
    public String showOrderPage(@RequestParam String key,
                                Model model) {

        OrderCheckoutResponse orderCheckoutResponse = orderApiClient.getOrderCheckout(key);
        // 주문 정보는 로그인/비회원 공통
        model.addAttribute("orderName", orderCheckoutResponse.orderName());
        model.addAttribute("cartItems", orderCheckoutResponse.bookItems());
        model.addAttribute("finalAmount", orderCheckoutResponse.finalAmount());
        model.addAttribute("packagingOptions", orderCheckoutResponse.packagingOptions());
        model.addAttribute("deliveryDates", orderCheckoutResponse.deliveryDateResponses());


        if (loginStatus.isLoggedIn()) {
            OrderUserResponse orderUserResponse = myOrderApiClient.getUserOrderInfo();
            model.addAttribute("ordererName", orderUserResponse.name());
            model.addAttribute("ordererEmail", orderUserResponse.email());
            model.addAttribute("ordererPhone", orderUserResponse.phone());

            // 주소, 포인트, 로그인 여부 등
            OrderUserResponse.AddressResponse defaultAddress = new OrderUserResponse.AddressResponse(
                    orderUserResponse.addressId(), null,
                    orderUserResponse.roadAddress(),
                    orderUserResponse.zipCode(),
                    orderUserResponse.addressDetail()
            );
            model.addAttribute("defaultAddress", defaultAddress);
            model.addAttribute("addressList", orderUserResponse.addressResponses());
            model.addAttribute("points", orderUserResponse.point());
            model.addAttribute("isGuest", !loginStatus.isLoggedIn());

            OrderCouponResponse orderCouponResponse = couponApiClient.orderCouponResponse(orderCheckoutResponse.bookItems());
            model.addAttribute("possibleCoupons", orderCouponResponse.possibleCoupons());
            model.addAttribute("impossibleCoupons", orderCouponResponse.impossibleCoupons());

        } else {
            // 토큰 없으면 비회원/게스트 처리
            model.addAttribute("isGuest", true);
        }



        return "order/order";
    }

    // 주문 저장 (주문과정 1번)
    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        OrderCreateResponse orderId = orderApiClient.saveOrder(orderCreateRequest);

        return ResponseEntity.ok(orderId);
    }

    @GetMapping("/{orderId}")
    public String showOrderDetail(@PathVariable long orderId,
                                  Model model) {

        OrderResponse orderResponse = orderApiClient.getOrderById(orderId);
        model.addAttribute("order", orderResponse);

        return "order/orderDetail";
    }

    @PostMapping("/guest")
    public String showGuestDetail(OrderTrackingRequest orderTrackingRequest,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        OrderResponse guestOrder = orderApiClient.getGuestOrder(orderTrackingRequest);

        if (guestOrder == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "입력하신 정보와 일치하는 주문을 찾을 수 없습니다. 정보를 다시 확인해 주세요.");
            return "redirect:/auth/login";
        }

        model.addAttribute("order", guestOrder);
        return "order/orderDetail";
    }


    /**
     * 배송 전 주문 취소 버튼(마이페이지)
     * */
    @PutMapping("/{orderId}/cancel")
    @ResponseBody
    public Map<String, Object> cancelOrder(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();

        try {
            orderApiClient.cancelOrder(orderId);

            response.put("success", true);
            response.put("message", "주문 취소 성공");
        } catch (Exception e) {
            log.error("Failed to cancel order {}: {}", orderId, e.getMessage());
            response.put("success", false);
            response.put("message", "주문 취소 처리 중 오류 발생: " + e.getMessage());
        }

        return response;
    }
}