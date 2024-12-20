package com.sparta.outsourcing_nt.dto.store.req;

import com.sparta.outsourcing_nt.entity.Store;
import com.sparta.outsourcing_nt.entity.StoreStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class StoreModifyRequestDto {
    @NotEmpty(message = "가게 이름은 필수 입력 항목입니다.")
    @Size(min = 1, max = 20, message = "가게 이름은 1글자 이상, 20글자 이하여야 합니다.")
    private String name;

    @NotEmpty(message = "카테고리는 필수 입력 항목입니다.")
    private String category;

    @NotEmpty(message = "주소는 필수 입력 항목입니다.")
    private String address;

    @NotEmpty(message = "전화번호는 필수 입력 항목입니다.")
    private String phone;

    @NotEmpty(message = "가게 소개는 필수 입력 항목입니다.")
    private String content;

    @Min(0)
    private int minDeliveryPrice;

    @Min(0)
    private int deliveryTip;

    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "영업 시작 시간은 HH:mm 형식으로 입력해야 합니다.")
    private String openTime;

    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "영업 마감 시간은 HH:mm 형식으로 입력해야 합니다.")
    private String closeTime;

    public Store toEntity() {
        return Store.builder()
                .name(name)
                .category(category)
                .address(address)
                .phone(phone)
                .content(content)
                .openTime(openTime)
                .closeTime(closeTime)
                .status(StoreStatus.ACTIVE)
                .build();
    }
}
