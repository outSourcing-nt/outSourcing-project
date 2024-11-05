package com.sparta.outsourcing_nt.dto.order.res;

import com.sparta.outsourcing_nt.entity.Order;
import com.sparta.outsourcing_nt.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private int totalPrice;
    private String requests;
    private String status;
    private Long storeId;
    private Long userId;
    private String statusCode;
    private String message;


    private List<MenuDto> menu;

    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.requests = order.getRequests();
        this.status = order.getStatus().getDescription();
        this.storeId = order.getStore().getId();
        this.userId = order.getUser().getId();
    }

    //상태 코드와 메시지를 설정할 수 있는 생성자
    public OrderResponseDto(Order order, String statusCode, String message) {
        this(order);
        this.statusCode = statusCode;
        this.message = message;
    }

    @Getter
    @AllArgsConstructor
    static class MenuDto {
        private Long menuId;
        private int price;
    }
}
