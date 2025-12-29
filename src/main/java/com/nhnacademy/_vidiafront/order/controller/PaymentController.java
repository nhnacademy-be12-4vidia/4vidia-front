package com.nhnacademy._vidiafront.order.controller;

import com.nhnacademy._vidiafront.global.auth.LoginStatus;
import com.nhnacademy._vidiafront.order.client.OrderApiClient;
import com.nhnacademy._vidiafront.order.client.PaymentApiClient;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderAmountResponse;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentConfirmRequest;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentFailRequest;
import com.nhnacademy._vidiafront.order.dto.payment.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderApiClient orderApiClient;
    private final PaymentApiClient paymentApiClient;
    private final LoginStatus loginStatus;

    @Value("${toss.clientKey}")
    private String TOSS_CLIENT_KEY;


    // tossPayment 결제 준비 (주문과정 2번)
    @GetMapping
    public String prepareTossPage(@RequestParam long orderId,
                                  @RequestParam String orderName,
                                  @RequestParam String paymentMethod,
                                  @RequestParam String ordererName,
                                  @RequestParam String ordererEmail,
                                  Model model) {

        String sendOrderId = "ORD-" + UUID.randomUUID();

        OrderAmountResponse orderAmountResponse = orderApiClient.getOrderPayPriceById(orderId);
        int payPrice = orderAmountResponse.payPrice();

        String safeOrderName = orderName;
        if (orderName.length() > 90) {
            safeOrderName = orderName.substring(0, 90) + "...";
        }

        model.addAttribute("tossClientKey", TOSS_CLIENT_KEY);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderName", safeOrderName);
        model.addAttribute("customerEmail", ordererEmail);
        model.addAttribute("customerName", ordererName);
        model.addAttribute("payPrice", payPrice);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("sendOrderId", sendOrderId);

        //자동결제 안쓰면 랜덤값도 가능
        String customerKey = UUID.randomUUID().toString();
        model.addAttribute("customerKey", customerKey);

        return "order/toss-js";
    }

    // 결제 이후 GET매핑 받아서 toss 저장(POST) (주문과정 3번)
    @GetMapping("/success")
    public String handlePaymentSuccess(@RequestParam String paymentKey,
                                       @RequestParam String orderId,
                                       @RequestParam int amount,
                                       @RequestParam long id) {

        PaymentConfirmRequest confirmRequest = new PaymentConfirmRequest(paymentKey, orderId, amount);

        paymentApiClient.confirmPayment(confirmRequest, id);

        return "redirect:/payments/success/" + id;
    }


    // 결제 성공 페이지 (주문과정 4번)
    @GetMapping("/success/{id}")
    public String successPayment(HttpServletRequest request,
                                 Model model,
                                 @PathVariable long id) {

        PaymentResponse paymentResponse = paymentApiClient.getPayment(id);
        model.addAttribute("payment", paymentResponse);
        model.addAttribute("isLoggedIn", loginStatus.isLoggedIn());

        return "order/orderSuccess";
    }

    // 결제 실패 페이지 (주문과정 4번)
    @GetMapping("/fail")
    public String failPayment(HttpServletRequest request, Model model) {

        model.addAttribute("code", request.getParameter("code"));
        model.addAttribute("message", request.getParameter("message"));

        return "order/orderFail";
    }

    // 결제창 뒤로가기 시 롤백 요청
    @PostMapping("/rollback")
    public ResponseEntity<Void> rollbackPayment(@RequestBody PaymentFailRequest request) {
        paymentApiClient.rollbackPayment(request);

        return ResponseEntity.ok().build();
    }


}
