package com.sparta.outsourcing_nt.entity;

import com.sparta.outsourcing_nt.dto.store.req.StoreCreateRequestDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "store")
@NoArgsConstructor
@RequiredArgsConstructor
public class Store extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int minDeliveryPrice;

    @Column(nullable = false)
    private int deliveryTip;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private int reviewCount;

    @Column(nullable = false)
    private String openTime;

    @Column(nullable = false)
    private String closeTime;

    @Column(nullable = false)
    private String status = "ACTIVE"; // 기본 상태를 ACTIVE로 해도 될지 검토 필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Store from(StoreCreateRequestDto reqDto) {
        Store store = new Store();
        store.initData(reqDto);
        return store;
    }

    private void initData(StoreCreateRequestDto reqDto) {
        this.name = reqDto.getName();
        this.category = reqDto.getCategory();
        this.address = reqDto.getAddress();
        this.phone = reqDto.getPhone();
        this.content = reqDto.getContent();
        this.minDeliveryPrice = reqDto.getMinDeliveryPrice();
        this.deliveryTip = reqDto.getDeliveryTip();
        this.openTime = reqDto.getOpenTime();
        this.closeTime = reqDto.getCloseTime();
    }

    public StoreResponseDto to() {
        return new StoreResponseDto(
                id,
                name,
                category,
                address,
                phone,
                content,
                minDeliveryPrice,
                deliveryTip,
                openTime,
                closeTime,
                getCreatedAt(),
                getModifiedAt(),
                status
        );
    }
}
