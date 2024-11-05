package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.dto.order.req.OrderRequestDto;
import com.sparta.outsourcing_nt.dto.order.res.OrderResponseDto;
import com.sparta.outsourcing_nt.entity.Order;
import com.sparta.outsourcing_nt.entity.Store;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.repository.OrderRepository;
import com.sparta.outsourcing_nt.repository.StoreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private Store store;

    public OrderResponseDto sendOrder(@Valid OrderRequestDto reqDto, User jwtUser) {
        Store store = storeRepository.findById(reqDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 영업 시간 확인
        if(!isStoreOpen(store)) {
            throw new IllegalArgumentException("가게의 오픈 시간이 아닙니다.");
        }
        // 최소 주문 금액 호가인
        if (reqDto.getTotalPrice() < store.getMinDeliveryPrice()) {
            throw new IllegalArgumentException("최소 주문 금액을 충족하지 않습니다.");
        }


        Order order = new Order();
        order.setTotalPrice(reqDto.getTotalPrice());
        order.setRequests(reqDto.getRequests());
        order.setStatus("ORDERED"); // 초기 상태 설정
        order.setUser(jwtUser);
        order.setStore(store);

        // 주문 저장
        Order createdOrder = orderRepository.save(order);


        logOrderAction("ORDER_CREATED", createdOrder);

        return new OrderResponseDto(createdOrder);

    }

    private boolean isStoreOpen(Store store) {
        LocalDateTime now = LocalDateTime.now();
        // 문자열을 LocalTime으로 변환 (예: "08:00" 형식일 경우)
        LocalTime openTime = LocalTime.parse(store.getOpenTime(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime closeTime = LocalTime.parse(store.getCloseTime(), DateTimeFormatter.ofPattern("HH:mm"));

        // 현재 시간을 LocalTime으로 변환하여 비교
        LocalTime currentTime = now.toLocalTime();

        return currentTime.isAfter(openTime) && currentTime.isBefore(closeTime);
    }


    // AOP에 의해 로그 남기는 메서드
    private void logOrderAction(String action, Order order) {
        // 로그에는 요청 시각, 가게 ID, 주문 ID 포함
        System.out.println("Action: " + action);
        System.out.println("Timestamp: " + LocalDateTime.now());
        System.out.println("Store ID: " + store.getId());
        System.out.println("Order ID: " + order.getId());
    }

    // 주문 ID 생성 메서드 (간단한 예시)
    private String generateOrderId() {
        return "ORD" + System.currentTimeMillis();
    }
}
