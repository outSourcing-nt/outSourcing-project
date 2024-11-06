package com.sparta.outsourcing_nt.service;

import com.sparta.outsourcing_nt.config.userdetails.AuthUserDetails;
import com.sparta.outsourcing_nt.dto.order.req.OrderRequestDto;
import com.sparta.outsourcing_nt.dto.order.res.OrderResponseDto;
import com.sparta.outsourcing_nt.entity.Order;
import com.sparta.outsourcing_nt.entity.OrderStatus;
import com.sparta.outsourcing_nt.entity.Store;
import com.sparta.outsourcing_nt.entity.User;
import com.sparta.outsourcing_nt.repository.OrderRepository;
import com.sparta.outsourcing_nt.repository.StoreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public OrderResponseDto sendOrder(@Valid OrderRequestDto reqDto) {
        // 현재 로그인한 사용자 정보를 SecurityContext에서 가져옵니다.
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User jwtUser = ((AuthUserDetails) userDetails).getUser();  // AuthUserDetails에서 User 객체를 추출


        Store store = storeRepository.findById(reqDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 영업 시간 확인
        if (!isStoreOpen(store)) {
            throw new IllegalArgumentException("가게의 오픈 시간이 아닙니다.");
        }
        // 최소 주문 금액 호가인
        if (reqDto.getTotalPrice() < store.getMinDeliveryPrice()) {
            throw new IllegalArgumentException("최소 주문 금액을 충족하지 않습니다.");
        }

        Order order = new Order();
        order.setTotalPrice(reqDto.getTotalPrice());
        order.setRequests(reqDto.getRequests());
        order.setStatus(OrderStatus.PREPARING); // 초기 상태 설정
        order.setUser(jwtUser);
        order.setStore(store);

        // 주문 저장
        Order createdOrder = orderRepository.save(order);

        // 로그 기록
        logOrderAction("ORDER_CREATED", createdOrder, store);

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
    private void logOrderAction(String action, Order order, Store store) {
        // 로그에는 요청 시각, 가게 ID, 주문 ID 포함
        System.out.println("Action: " + action);
        System.out.println("Timestamp: " + LocalDateTime.now());
        System.out.println("Store ID: " + store.getId());
        System.out.println("Order ID: " + order.getId());
    }

    public List<OrderResponseDto> getOrderList() {
        // Order 엔티티 목록을 조회
        List<Order> orders = orderRepository.findAll();

        // Order 엔티티 목록을 OrderResponseDto로 변환
        return orders.stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    // 주문 ID로 주문 상세 정보 조회
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다. 주문 ID: " + orderId));
        return new OrderResponseDto(order);
    }

    // 주문 상태 업데이트
    public OrderResponseDto updateOrderStatus(Long storeId, Long orderId, OrderStatus status) {
        // Store가 존재하는지 확인
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        // Order가 존재하는지 확인
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        // 상태 업데이트
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        // 상태 변경 시 로그 기록
        logOrderAction("ORDER_STATUS_UPDATED", updatedOrder, store);

        return new OrderResponseDto(updatedOrder);
    }


}
