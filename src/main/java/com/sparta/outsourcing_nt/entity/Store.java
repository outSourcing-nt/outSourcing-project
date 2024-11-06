package com.sparta.outsourcing_nt.entity;

import com.sparta.outsourcing_nt.dto.store.req.StoreModifyRequestDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "store")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public StoreResponseDto toResponseDto() {
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

    public void modifyData(StoreModifyRequestDto reqDto) {
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

    public void close() {
        this.status = StoreStatus.CLOSED;
        setDeletedAt(LocalDateTime.now());
    }
}
