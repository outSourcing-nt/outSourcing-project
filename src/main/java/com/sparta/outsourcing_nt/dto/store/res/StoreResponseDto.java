package com.sparta.outsourcing_nt.dto.store.res;

import com.sparta.outsourcing_nt.entity.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StoreResponseDto {
    private Long id;
    private String name;
    private String category;
    private String address;
    private String phone;
    private String content;
    private int minDeliveryPrice;
    private int deliveryTip;
    private String openTime;
    private String closeTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private StoreStatus status;
}
