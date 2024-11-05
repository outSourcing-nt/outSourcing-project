package com.sparta.outsourcing_nt.controller;

import com.sparta.outsourcing_nt.dto.order.req.OrderRequestDto;
import com.sparta.outsourcing_nt.dto.order.res.OrderResponseDto;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;


    //주문하기
    @PostMapping("/store/{storeId}/order")
    public ResponseEntity<OrderResponseDto> sendOrder(
            @Valid @RequestBody OrderRequestDto reqDto,
            @RequestAttribute("user") User jwtUser) {
                OrderResponseDto resDto = orderService.sendOrder(reqDto, jwtUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(resDto);
    }


}
