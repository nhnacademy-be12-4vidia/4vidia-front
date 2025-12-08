package com.nhnacademy._vidiafront.order.controller;

import com.nhnacademy._vidiafront.order.client.OrderApiClient;
import com.nhnacademy._vidiafront.order.client.PaymentApiClient;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCheckoutRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCreateRequest;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderPageRequest;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCheckoutResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCreateResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderResponse;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentConfirmRequest;
import com.nhnacademy._vidiafront.order.dto.payment.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApiClient orderApiClient;
    private final PaymentApiClient paymentApiClient;

    @Value("${toss.clientKey}")
    private String TOSS_CLIENT_KEY;

    @PostMapping
    public String showOrderPage(@ModelAttribute OrderPageRequest orderPageRequest,
                                Model model) {

        List<OrderCheckoutRequest> orderCheckoutRequests;

        if (orderPageRequest.orderCheckoutRequests() != null) {
            orderCheckoutRequests =  orderPageRequest.orderCheckoutRequests();
        } else {
            orderCheckoutRequests = List.of(OrderCheckoutRequest.from(orderPageRequest.bookId(), orderPageRequest.quantity()));
        }

        OrderCheckoutResponse response = orderApiClient.getOrderCheckout(orderCheckoutRequests);

        model.addAttribute("ordererName", response.name());
        model.addAttribute("ordererEmail", response.email());
        model.addAttribute("ordererPhone", response.phone());
        model.addAttribute("addressList", response.addressResponses());
        model.addAttribute("points", response.point());

        model.addAttribute("orderName", response.orderName());
        model.addAttribute("cartItems", response.bookItems());
        model.addAttribute("finalAmount", response.finalAmount()); // (첵 판매가 * 수량)의 합

        model.addAttribute("possibleCoupons", response.possibleCoupons());
        model.addAttribute("impossibleCoupons", response.impossibleCoupons());
        model.addAttribute("packagingOptions", response.packagingOptions());
        model.addAttribute("deliveryDates", response.deliveryDateResponses());

        return "order/order";
    }

    @PostMapping("/create") // 주문 저장
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        OrderCreateResponse orderId = orderApiClient.saveOrder(orderCreateRequest); //주문과정 1번

        return ResponseEntity.ok(orderId);
    }

    @GetMapping("/toss-prepare") //tossPayment 결제 준비
    public String prepareTossPage(@RequestParam long orderId,
                                  @RequestParam String orderName,
                                  @RequestParam String paymentMethod,
                                  @RequestParam int payPrice,
                                  Model model) {

        String sendOrderId = "ORD-" + UUID.randomUUID(); //주문과정 2번

        model.addAttribute("tossClientKey", TOSS_CLIENT_KEY);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderName", orderName);
        model.addAttribute("customerEmail", "what@email.com"); //TODO order.html에서 값이 넘어와야할듯?
        model.addAttribute("customerName", "홍길동");
        model.addAttribute("payPrice", payPrice);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("sendOrderId", sendOrderId);

        //자동결제 안쓰면 랜덤값도 가능
        String customerKey = UUID.randomUUID().toString();
        model.addAttribute("customerKey", customerKey);

        return "order/toss-js";
    }

    @GetMapping("/{orderId}")
    public String showOrderDetail(@PathVariable long orderId,
                                  Model model) {

        OrderResponse orderResponse = orderApiClient.getOrderById(orderId);
        model.addAttribute("order", orderResponse);

        return "order/orderDetail";
    }

    @GetMapping("/{id}/success")
    public String handlePaymentSuccess(@RequestParam String paymentKey,
                                       @RequestParam String orderId,
                                       @RequestParam int amount,
                                       @PathVariable long id) {

        PaymentConfirmRequest confirmRequest = new PaymentConfirmRequest(paymentKey, orderId, amount); //주문과정 3번

        paymentApiClient.confirmPayment(confirmRequest, id);

        return "redirect:/orders/success/" + id;
    }

    @GetMapping("/success/{orderId}")
    public String successOrder(@PathVariable long orderId,
                               Model model) {
        PaymentResponse paymentResponse = paymentApiClient.getPayment(orderId); //주문과정 6번
        model.addAttribute("payment", paymentResponse);

        return "order/orderSuccess";
    }


    @GetMapping("/fail")
    public String failPayment(HttpServletRequest request, Model model) {

        model.addAttribute("code", request.getParameter("code"));
        model.addAttribute("message", request.getParameter("message"));

        return "order/orderFail";
    }




}