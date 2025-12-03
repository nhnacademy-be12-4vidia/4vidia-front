package com.nhnacademy._vidiafront.order.controller;

import com.nhnacademy._vidiafront.order.config.OrderApiClient;
import com.nhnacademy._vidiafront.order.config.PaymentApiClient;
import com.nhnacademy._vidiafront.order.dto.order.request.OrderCreateRequest;
import com.nhnacademy._vidiafront.order.dto.order.response.DeliveryDateResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderCreateResponse;
import com.nhnacademy._vidiafront.order.dto.order.response.OrderResponse;
import com.nhnacademy._vidiafront.order.dto.packaging.response.PackagingOptionResponse;
import com.nhnacademy._vidiafront.order.dto.payment.requset.PaymentConfirmRequest;
import com.nhnacademy._vidiafront.order.dto.payment.response.PaymentResponse;
import com.nhnacademy._vidiafront.user.client.AddressApiClient;
import com.nhnacademy._vidiafront.user.client.UserApiClient;
import com.nhnacademy._vidiafront.user.dto.address.response.AddressResponse;
import com.nhnacademy._vidiafront.user.dto.user.response.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApiClient orderApiClient;
    private final UserApiClient userApiClient;
    private final PaymentApiClient paymentApiClient;
    private final AddressApiClient addressApiClient;
    //private final BookApiClient bookApiClient;

    @Value("${toss.clientKey}")
    private String TOSS_CLIENT_KEY;

    // TODO 삭제! OrderController에서 사용할 임시 DTO 구조
    public record CartItemDisplayDto(
            Long id,
            BookDisplayDto book,
            int price, // 상품 단가
            int quantity,
            int totalPrice // 상품 소계 (단가 * 수량)
    ) {
        public record BookDisplayDto(
                Long id,
                String title,
                String author,
                String publisher,
                String imageUrl,
                Integer salePrice
        ) {}
    }

    @GetMapping
    public String showOrderPage(@RequestHeader(value = "X-User-Id", required = false) Long memberId,
                                @RequestHeader(value = "X-Guest-Id", required = false) Long guestId,
                                @RequestParam Boolean direct, // 바로 주문인지 장바구니에서 오는지
                                @RequestParam long bookId,
                                @RequestParam int quantity,
                                Model model) {
        if (memberId != null){ // 회원일 경우

            UserProfileResponse userProfile = userApiClient.getUserProfile();

            List<AddressResponse> addressResponses = addressApiClient.getAddresseList();

            model.addAttribute("ordererName", userProfile.name());
            model.addAttribute("ordererEmail", userProfile.email());
            model.addAttribute("ordererPhone", userProfile.phone());
            model.addAttribute("addressList", addressResponses);
            model.addAttribute("points", userProfile.point());

            //TODO 유저의 보유쿠폰 리스트 (적용가능한것과 불가능한것 리스트)
            // 그러기 위해 유저아이디, 구매하는 책 종류와 총금액 보내줘야하나?
            model.addAttribute("possibleCoupons", new ArrayList<>());
            model.addAttribute("impossibleCoupons", new ArrayList<>());
        }

        List<CartItemDisplayDto> cartItems = new ArrayList<>();
        if (direct) { //바로 주문일 떄
            // TODO bookId로 책 정보 가져와서 수량까지 담아 보내기
            //bookApiClient.
            cartItems.add(new CartItemDisplayDto(
                    1001L,
                    new CartItemDisplayDto.BookDisplayDto(
                            5L, "스프링 완벽 가이드", "로드 존슨", "비디아 출판", "https://example.com/cover1.jpg", 35000
                    ),
                    35000,
                    1,
                    35000
            ));

        } else { //장바구니에서 왔을 때
            //TODO cart를 레디스에서 꺼냄 - 일단 임시데이터 바로 주문 도서 종류 1개 수량 존재
            cartItems.add(new CartItemDisplayDto(
                    1001L,
                    new CartItemDisplayDto.BookDisplayDto(
                            5L, "스프링 완벽 가이드", "로드 존슨", "비디아 출판", "https://example.com/cover1.jpg", 35000
                    ),
                    35000,
                    1,
                    35000
            ));
            // 상품 2: 자바 마이크로 서비스 패턴 (2개)
            cartItems.add(new CartItemDisplayDto(
                    1002L,
                    new CartItemDisplayDto.BookDisplayDto(
                            8L, "자바 마이크로 서비스 패턴", "에릭 에반스", "MSA 출판", null,  20000
                    ),
                    20000,
                    2,
                    40000
            ));
        }


        //책 * 수량 최종 금액
        int finalAmount = cartItems.stream()
                .mapToInt(item -> item.totalPrice)
                .sum();

        //담은 책 종류에 따라 주문명 변경
        String orderName = "";
        if (cartItems.size() == 1) {
            orderName = cartItems.getFirst().book.title;
        }else {
            orderName = cartItems.getFirst().book.title + " 외 " + (cartItems.size() - 1) + "권";
        }

        //패키지 옵션, 배송가능날짜
        List<PackagingOptionResponse> packagingOptions = orderApiClient.getPackagingOptions();
        List<DeliveryDateResponse> deliveryDateResponses = orderApiClient.getDeliveryDates();

        model.addAttribute("finalAmount", finalAmount); //null값??? 또는 html 확인
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("orderName", orderName);
        model.addAttribute("packagingOptions", packagingOptions);
        model.addAttribute("deliveryDates", deliveryDateResponses);


        return "order/order";
    }

    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        OrderCreateResponse orderId = orderApiClient.saveOrder(orderCreateRequest); //주문과정 1번

        return ResponseEntity.ok(orderId);
    }

    @GetMapping("/toss-prepare")
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

        String customerKey = UUID.randomUUID().toString();

        //자동결제 안쓰면 랜덤값도 가능
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

        paymentApiClient.confirmPayment(confirmRequest, id);//

        return "redirect:/orders/success/" + id; //
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