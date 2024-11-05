package com.sparta.outsourcing_nt.dto.order.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "총 주문 금액을 입력해야 합니다.")
    @Min(value = 1, message = "총 주문 금액은 최소 {value} 이상이어야 합니다.")
    private int totalPrice;

    @NotBlank(message = "요청 사항을 입력해 주세요.")
    private String requests; // 요구 사항

    @NotNull(message = "가게 ID를 입력해 주세요.")
    private Long storeId;

    @NotNull(message = "메뉴 항목을 입력해 주세요.")
    private List<MenuItem> menuItems;

    @Getter
    @NoArgsConstructor
    public static class MenuItem {
        @NotNull(message = "메뉴 ID를 입력해 주세요.")
        private Long menuId;

        @NotNull(message = "수량을 입력해 주세요.")
        @Min(value = 1, message = "수량은 최소 {value} 이상이어야 합니다.")
        private int quantity;
    }
}
