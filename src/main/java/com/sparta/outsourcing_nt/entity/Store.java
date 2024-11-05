package com.sparta.outsourcing_nt.entity;

import com.sparta.outsourcing_nt.dto.store.req.StoreCreateRequestDto;
import com.sparta.outsourcing_nt.dto.store.res.StoreResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "store")
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
    private String status;

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
}
