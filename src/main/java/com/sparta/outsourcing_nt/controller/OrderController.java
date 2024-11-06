package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.order.req.OrderRequestDto;
import com.sparta.outsourcing_nt.dto.order.req.OrderStatusUpdateRequestDto;
import com.sparta.outsourcing_nt.dto.order.res.OrderResponseDto;
import com.sparta.outsourcing_nt.entity.Order;
import com.sparta.outsourcing_nt.entity.OrderStatus;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.service.OrderService;
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
    public ResponseEntity<OrderResponseDto> sendOrder(
            @Valid @RequestBody OrderRequestDto reqDto,
            @AuthenticationPrincipal User jwtUser) {

                OrderResponseDto resDto = orderService.sendOrder(reqDto, jwtUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
    }

    // 전체 주문 목록 조회
    @GetMapping("/store/{storeId}/orders")
    public ResponseEntity<List<OrderResponseDto>> getOrderList() {
        List<OrderResponseDto> orderList = orderService.getOrderList();
        return ResponseEntity.ok(orderList);
    }

    // 특정 주문 상세 정보 조회
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        OrderResponseDto orderDetails = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDetails);
    }

    // 주문 상태 수정
    @PutMapping("/store/{storeId}/order/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
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

        OrderResponseDto updatedOrder = orderService.updateOrderStatus(storeId, orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}
