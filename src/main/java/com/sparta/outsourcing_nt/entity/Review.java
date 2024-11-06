package com.sparta.outsourcing_nt.entity;

import com.sparta.outsourcing_nt.dto.review.req.ReviewRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "review")
@NoArgsConstructor
//@RequiredArgsConstructor
public class Review extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Review(ReviewRequestDto requestDto, Store store, User user, Order order) {
        this.rating = requestDto.getRating();
        this.content = requestDto.getContent();
        this.store = store;
        this.user = user;
        this.order = order;
    }
}
