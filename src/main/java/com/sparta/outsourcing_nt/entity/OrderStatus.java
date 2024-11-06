package com.sparta.outsourcing_nt.entity;

public enum OrderStatus {
    PREPARING("접수 전"),
    ACCEPTED("접수"),
    IN_DELIVERY("배달중"),
    COMPLETED("완료"),
    CANCELED("주문삭제");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    private OrderStatus parseOrderStatus(String status) {
        try {
            return OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 주문 상태입니다: " + status);
        }
    }
}
