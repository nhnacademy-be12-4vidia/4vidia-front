package com.nhnacademy._vidiafront.order.controller;

import com.nhnacademy._vidiafront.order.client.PaymentApiClient;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentConfirmRequest;
import com.nhnacademy._vidiafront.order.dto.payment.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentApiClient paymentApiClient;

    @Value("${toss.clientKey}")
    private String TOSS_CLIENT_KEY;


    // tossPayment 결제 준비 (주문과정 2번)
    @GetMapping
    public String prepareTossPage(@RequestParam long orderId,
                                  @RequestParam String orderName,
                                  @RequestParam String paymentMethod,
                                  @RequestParam int payPrice,
                                  Model model) {

        String sendOrderId = "ORD-" + UUID.randomUUID();

        model.addAttribute("tossClientKey", TOSS_CLIENT_KEY);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderName", orderName);
        model.addAttribute("customerEmail", "");
        model.addAttribute("customerName", "홍길동");
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

        return "order/orderSuccess";
    }

    // 결제 실패 페이지 (주문과정 4번)
    @GetMapping("/fail")
    public String failPayment(HttpServletRequest request, Model model) {

        model.addAttribute("code", request.getParameter("code"));
        model.addAttribute("message", request.getParameter("message"));

        return "order/orderFail";
    }

}
