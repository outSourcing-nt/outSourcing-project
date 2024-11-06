package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.order.req.OrderRequestDto;
import com.sparta.outsourcing_nt.dto.order.req.OrderStatusUpdateRequestDto;
import com.sparta.outsourcing_nt.dto.order.res.OrderResponseDto;
import com.sparta.outsourcing_nt.entity.Order;
import com.sparta.outsourcing_nt.entity.OrderStatus;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.service.OrderService;
import com.sparta.outsourcing_nt.util.result.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;


    //주문하기
    @PostMapping("/store/{storeId}/order")
    public ResponseEntity<ApiResult<OrderResponseDto>> sendOrder(
            @Valid @RequestBody OrderRequestDto reqDto, @AuthenticationPrincipal AuthUserDetails authUser) {

                return new ResponseEntity<>(
                        ApiResult.success("주문하기 성공",
                                orderService.sendOrder(reqDto,authUser)),
                        HttpStatus.OK);
    }

    // 전체 주문 목록 조회
    @GetMapping("/store/{storeId}/orders")
    public ResponseEntity<ApiResult<List<OrderResponseDto>>> getOrderList() {
        return new ResponseEntity<>(
                ApiResult.success("전체 주문 목록 조회하기 성공",
                        orderService.getOrderList()),
                HttpStatus.OK);

    }

    // 특정 주문 상세 정보 조회
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResult<OrderResponseDto>> getOrderById(@PathVariable Long orderId) {
        return new ResponseEntity<>(
                ApiResult.success("특정 주문 상세 정보 조회하기 성공",
                        orderService.getOrderById(orderId)),
                HttpStatus.OK);
    }

    // 주문 상태 수정
    @PutMapping("/store/{storeId}/order/{orderId}")
    public ResponseEntity<ApiResult<OrderResponseDto>> updateOrderStatus(
            @PathVariable Long storeId,
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequestDto requestDto
            ) {
        // OrderStatus로 변환
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(requestDto.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 주문 상태입니다: " + requestDto.getStatus());
        }

        return new ResponseEntity<>(
                ApiResult.success("주문 상태 수정하기 성공",
                        orderService.updateOrderStatus(storeId, orderId, status)),
                HttpStatus.OK);
    }
}
